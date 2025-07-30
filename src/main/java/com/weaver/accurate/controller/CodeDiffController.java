package com.weaver.accurate.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

import com.weaver.accurate.analy.Bean.MethodInfo;
import com.weaver.accurate.analy.strategy.DeduceApiService;
import com.weaver.accurate.common.OrikaMapperUtils;
import com.weaver.accurate.dto.code.ApiModify;
import com.weaver.accurate.dto.code.DiffClassInfoResult;
import com.weaver.accurate.dto.code.DiffMethodParams;
import com.weaver.accurate.dto.code.MethodInvokeParam;
import com.weaver.accurate.enums.CodeManageTypeEnum;
import com.weaver.accurate.response.ApiResponse;
import com.weaver.accurate.response.UniqueApoResponse;
import com.weaver.accurate.result.CodeDiffResultVO;
import com.weaver.accurate.result.DeduceApiVO;
import com.weaver.accurate.result.MethodInfoVO;
import com.weaver.accurate.service.CodeDiffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@Api(value = "/api/code/diff", tags = "差异代码模块")
@RequestMapping("/api/code/diff")
@Validated
public class CodeDiffController {

    @Autowired
    private CodeDiffService codeDiffService;

    @Autowired
    protected DeduceApiService deduceApiService;

    @ApiOperation("git获取差异代码")
    @GetMapping(value = "git/list")
    public UniqueApoResponse<List<CodeDiffResultVO>> getGitList(
            @ApiParam(required = true, name = "gitUrl", value = "git远程仓库地址")
            @NotEmpty
            @RequestParam(value = "gitUrl") String gitUrl,
            @ApiParam(required = true, name = "baseVersion", value = "git原始分支或tag")
            @NotEmpty
            @RequestParam(value = "baseVersion") String baseVersion,
            @ApiParam(required = true, name = "nowVersion", value = "git现分支或tag")
            @NotEmpty
            @RequestParam(value = "nowVersion") String nowVersion) {
        DiffMethodParams diffMethodParams = DiffMethodParams.builder()
                .repoUrl(StringUtils.trim(gitUrl))
                .baseVersion(StringUtils.trim(baseVersion))
                .nowVersion(StringUtils.trim(nowVersion))
                .codeManageTypeEnum(CodeManageTypeEnum.GIT)
                .build();
        List<DiffClassInfoResult> diffCodeList = codeDiffService.getDiffCode(diffMethodParams).getDiffClasses();
        List<CodeDiffResultVO> list = OrikaMapperUtils.mapList(diffCodeList, DiffClassInfoResult.class, CodeDiffResultVO.class);
        return new UniqueApoResponse<List<CodeDiffResultVO>>().success(list, JSON.toJSONString(list, SerializerFeature.WriteNullListAsEmpty));
    }


    @ApiOperation("git获取影响接口")
    @RequestMapping(value = "git/deduce/api", method = RequestMethod.GET)
    public ApiResponse getGitDeduceApiList(
            @ApiParam(required = true, name = "gitUrl", value = "git远程仓库地址")
            @NotEmpty
            @RequestParam(value = "gitUrl") String gitUrl,
            @ApiParam(required = true, name = "baseVersion", value = "git原始分支或tag")
            @NotEmpty
            @RequestParam(value = "baseVersion") String baseVersion,
            @ApiParam(required = true, name = "nowVersion", value = "git现分支或tag")
            @NotEmpty
            @RequestParam(value = "nowVersion") String nowVersion) {
        DiffMethodParams diffMethodParams = DiffMethodParams.builder()
                .repoUrl(StringUtils.trim(gitUrl))
                .baseVersion(StringUtils.trim(baseVersion))
                .nowVersion(StringUtils.trim(nowVersion))
                .codeManageTypeEnum(CodeManageTypeEnum.GIT)
                .build();
        ApiModify apiModify = deduceApiService.deduceApi(diffMethodParams);
//        DeduceApiVO deduceApiVO = OrikaMapperUtils.map(apiModify, DeduceApiVO.class);
//        return new ApiResponse<DeduceApiVO>().success(deduceApiVO);
        return new ApiResponse(200, "正在分析请稍后");
    }


    @ApiOperation("获取git调用链")
    @RequestMapping(value = "git/method/link", method = RequestMethod.GET)
    public ApiResponse<Map<String, List<MethodInfoVO>>> getGitMethodLink(
            @ApiParam(required = true, name = "gitUrl", value = "git远程仓库地址")
            @NotEmpty
            @RequestParam(value = "gitUrl") String gitUrl,
            @ApiParam(required = true, name = "branchName", value = "git分支或tag")
            @NotEmpty
            @RequestParam(value = "branchName") String branchName) {
        MethodInvokeParam methodInvokeParam = MethodInvokeParam.builder().repoUrl(gitUrl).branchName(branchName).codeManageTypeEnum(CodeManageTypeEnum.GIT).build();
        Map<String, List<MethodInfo>> staticMethodInvoke = codeDiffService.getStaticMethodInvoke(methodInvokeParam);
        Map<String, List<MethodInfoVO>> map = new HashMap<>();
        staticMethodInvoke.forEach((k, v) -> {
            map.put(k, OrikaMapperUtils.mapList(v, MethodInfo.class, MethodInfoVO.class));
        });
        return new ApiResponse<Map<String, List<MethodInfoVO>>>().success(map);
    }



}
