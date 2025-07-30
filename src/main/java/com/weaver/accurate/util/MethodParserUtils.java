package com.weaver.accurate.util;

import cn.hutool.crypto.SecureUtil;
import com.github.javaparser.JavaParser;
import com.github.javaparser.Range;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.CallableDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.weaver.accurate.common.BizCode;
import com.weaver.accurate.common.BizException;
import com.weaver.accurate.common.LoggerUtil;
import com.weaver.accurate.dto.code.MethodInfoResult;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @ProjectName: base-service
 * @Package: com.dr.codediff.util
 * @Description: 解析获取类的方法
 * @Author: duanrui
 * @CreateDate: 2021/1/8 21:06
 * @Version: 1.0
 * <p>
 * Copyright: Copyright (c) 2021
 */
@Slf4j
public class MethodParserUtils {

    /**
     * 解析类获取类的所有方法
     *
     * @param classFile
     * @return
     */
    public static List<MethodInfoResult> parseMethods(String classFile, String rootCodePath) {
        List<MethodInfoResult> list = new ArrayList<>();
        try (FileInputStream in = new FileInputStream(classFile)) {
            // 使用JavaParser解析类文件
            JavaParser javaParser = new JavaParser();
            //JavaParser 解析输入流 in（表示一个 Java 源文件），并获取解析结果 CompilationUnit（抽象语法树的根节点）。如果解析失败，则抛出自定义异常 BizException
            CompilationUnit cu = javaParser.parse(in).getResult().orElseThrow(() -> new BizException(BizCode.PARSE_JAVA_FILE));
            removeComments(cu);
            cu.accept(new MethodVisitor(), list);
            return list;
        } catch (IOException e) {
            LoggerUtil.error(log, "读取class类失败", e);
            throw new BizException(BizCode.PARSE_JAVA_FILE);
        }

    }

    /**
     * @param compilationUnit
     */
    private static void removeComments(CompilationUnit compilationUnit) {
        List<Comment> comments = compilationUnit.getAllContainedComments();
        List<Comment> unwantedComments = comments
                .stream()
                .filter(MethodParserUtils::isValidCommentType)
                .collect(Collectors.toList());
        unwantedComments.forEach(Comment::remove);
    }

    /**
     * 我们只识别单行注释和块注释
     *
     * @param comment
     * @return true if meet the correct type
     */
    private static boolean isValidCommentType(Comment comment) {
        return comment instanceof LineComment || comment instanceof BlockComment;
    }


    /**
     * javaparser工具类核心方法，主要通过这个类遍历class文件的方法，此方法主要是获取出代码的所有方法，然后再去对比方法是否存在差异
     */
    private static class MethodVisitor extends VoidVisitorAdapter<List<MethodInfoResult>> {


        /**
         * 构造函数变更
         *
         * @param n
         * @param list
         */
        @Override
        public void visit(final ConstructorDeclaration n, List<MethodInfoResult> list) {
            buildMethod(n, list);
            super.visit(n, list);
        }

        @Override
        public void visit(MethodDeclaration m, List<MethodInfoResult> list) {
            buildMethod(m, list);
            super.visit(m, list);
        }

        @Override
        public void visit(final LambdaExpr n, List<MethodInfoResult> list) {
            super.visit(n, list);
        }
        private void buildMethod(CallableDeclaration m, List<MethodInfoResult> list) {
            //删除外部注释
            m.removeComment();
            //删除方法内行注释
            List<Comment> comments = m.getAllContainedComments();
            for (Comment comment : comments) {
                comment.remove();
            }
            //计算方法体的hash值，疑问，空格，特殊转义字符会影响结果，导致相同匹配为差异？建议提交代码时统一工具格式化
            String md5 = SecureUtil.md5(m.toString());
            NodeList<Parameter> parameters = m.getParameters();
            List<String> params = parameters.stream().map(e -> {
                String paramType =e.getType().toString().trim();
                if (e.getType().isClassOrInterfaceType()) {
                    paramType = e.getType().asClassOrInterfaceType().getNameAsString();
                }
                //数组类型
                if(e.isVarArgs()){
                    paramType = paramType + "[]";
                }
                return paramType;

            }).collect(Collectors.toList());
            int startLine = 0;
            int endLine = 0;
            Range range = m.getRange().orElse(null);
            if (null != range) {
                startLine = range.begin.line;
                endLine = range.end.line;
            }
            MethodInfoResult result = MethodInfoResult.builder()
                    .md5(md5)
                    .methodName(m.getNameAsString())
                    .parameters(params)
                    .startLine(startLine)
                    .endLine(endLine)
                    .build();
            list.add(result);
        }


    }
}
