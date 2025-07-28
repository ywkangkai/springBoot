package com.weaver.accurate.vercontrol;
import com.weaver.accurate.dto.code.DiffInfo;
import com.weaver.accurate.dto.code.MethodInvokeDto;
import com.weaver.accurate.dto.code.VersionControlDto;
import com.weaver.accurate.enums.CodeManageTypeEnum;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
public class VersionControlHandlerFactory implements CommandLineRunner, ApplicationContextAware {
    private volatile ApplicationContext applicationContext;


    private static Map<String, AbstractVersionControl> handlerMap;


    /**
     * 封装策略
     *
     * @param args
     */
    @Override
    public void run(String... args) {
        Collection<AbstractVersionControl> checkHandlers = this.applicationContext.getBeansOfType(AbstractVersionControl.class).values();
        setHandlerMap(checkHandlers.stream().collect(Collectors.toMap(e -> e.getType().getValue(), Function.identity())));
    }

    /**
     * 设置应用程序上下文
     *
     * @param applicationContext 应用程序上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    /**
     * 设置处理Map
     *
     * @param handlerMap
     */
    private static void setHandlerMap(Map<String, AbstractVersionControl> handlerMap) {
        VersionControlHandlerFactory.handlerMap = handlerMap;
    }

    /**
     * 执行方法校验
     *
     * @param versionControlDto
     */
    public static DiffInfo processHandler(VersionControlDto versionControlDto) {
        CodeManageTypeEnum codeManageTypeEnum = versionControlDto.getCodeManageTypeEnum();
        if (handlerMap.containsKey(codeManageTypeEnum.getValue())) {
            return handlerMap.get(codeManageTypeEnum.getValue()).handler(versionControlDto);
        }
        return null;
    }


    public static String downloadCode(MethodInvokeDto methodInvokeDto) {
        CodeManageTypeEnum codeManageTypeEnum = methodInvokeDto.getCodeManageTypeEnum();
        if (handlerMap.containsKey(codeManageTypeEnum.getValue())) {
            return handlerMap.get(codeManageTypeEnum.getValue()).downloadCode(methodInvokeDto);
        }
        return null;
    }
}
