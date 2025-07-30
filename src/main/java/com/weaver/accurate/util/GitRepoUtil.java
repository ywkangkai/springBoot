package com.weaver.accurate.util;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.weaver.accurate.common.BizCode;
import com.weaver.accurate.common.BizException;
import com.weaver.accurate.common.LoggerUtil;
import com.weaver.accurate.enums.GitUrlTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.*;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.FileUtils;
import org.springframework.util.StringUtils;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import java.io.File;
import java.io.IOException;


@Slf4j
public class GitRepoUtil {


    private static final  Integer COMMIT_ID_LENGTH = 40;

    public static Git instanceHttpGit(String gitUrl, String codePath, String commitId, Git git, String gitUserName, String gitPassWord) throws GitAPIException {
        if (null != git) {
            git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUserName, gitPassWord)).call();
            return git;
        }
        CloneCommand gits = Git.cloneRepository()
                .setURI(gitUrl)
                .setDirectory(new File(codePath))

                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUserName, gitPassWord));
        //说明不是commit_id
        if(COMMIT_ID_LENGTH != commitId.length()){
            gits.setBranch(commitId);
        }
        return gits.call();
    }


    public static Git instanceSshGit(String gitUrl, String codePath, String commitId, Git git, String privateKey) throws GitAPIException {
        SshSessionFactory sshSessionFactory = new JschConfigSessionFactory() {
            @Override
            protected JSch createDefaultJSch(FS fs) throws JSchException {
                JSch defaultJSch = super.createDefaultJSch(fs);
                defaultJSch.addIdentity(privateKey);
                return defaultJSch;
            }

            @Override
            public void configure(OpenSshConfig.Host hc, Session session) {
                session.setConfig("StrictHostKeyChecking", "no");
            }
        };

        if (null != git) {
            git.pull().setTransportConfigCallback(transport -> {
                SshTransport sshTransport = (SshTransport) transport;
                sshTransport.setSshSessionFactory(sshSessionFactory);
            }).call();
            return git;
        }
        CloneCommand cloneCommand = Git.cloneRepository()
                .setURI(gitUrl)
                .setTransportConfigCallback(transport -> {
                    SshTransport sshTransport = (SshTransport) transport;
                    sshTransport.setSshSessionFactory(sshSessionFactory);
                })
                .setDirectory(new File(codePath));
        //说明不是commit_id
        if(COMMIT_ID_LENGTH != commitId.length()){
            cloneCommand.setBranch(commitId);
        }
        return cloneCommand.call();
    }

    /**
     * 克隆代码到本地，http/s方式拉取代码
     *
     * @param gitUrl      git url
     * @param codePath    代码路径
     * @param branchName  分支名称
     * @param gitUserName git用户名
     * @param gitPassWord git密码
     * @return {@link Git}
     */
    public static Git httpCloneRepository(String gitUrl, String codePath, String branchName, String gitUserName, String gitPassWord) {
        Git git = null;
        try {
            if (!checkGitWorkSpace(gitUrl, codePath)) {
                LoggerUtil.info(log, "本地代码不存在，clone", gitUrl, codePath);
                git = instanceHttpGit(gitUrl, codePath, branchName, null, gitUserName, gitPassWord);
                // 下载指定分支,如果是commitId在上一步已经下载了
                if (git.getRepository().exactRef(Constants.HEAD).isSymbolic()){
                    git.checkout().setName(branchName).call();
                }
            } else {
                LoggerUtil.info(log, "本地代码存在,直接使用", gitUrl, codePath);
                git = Git.open(new File(codePath));
                git.getRepository().getFullBranch();
                //判断是分支还是commitId，分支做更新，commitId无法改变用原有的
                if (git.getRepository().exactRef(Constants.HEAD).isSymbolic()) {
                    //更新分支代码
                    instanceHttpGit(gitUrl, codePath, branchName, git, gitUserName, gitPassWord);
                }
            }
        } catch (IOException | GitAPIException e) {
            if (e instanceof GitAPIException) {
                throw new BizException(BizCode.GIT_AUTH_FAILED.getCode(), e.getMessage());
            }
            e.printStackTrace();
            throw new BizException(BizCode.GIT_OPERATED_FAIlED);
        }
        return git;
    }

    /**
     * ssh克隆存储库
     *
     * @param gitUrl     git url
     * @param codePath   代码路径
     * @param branchName 分支名称
     * @param privateKey 私钥
     * @return {@link Git}
     * @date:2021/9/8
     * @className:GitRepoUtil
     * @author:Administrator
     * @description: ssh方式拉取代码
     */
    public static Git sshCloneRepository(String gitUrl, String codePath, String branchName, String privateKey) {
        Git git = null;
        try {
            if (!checkGitWorkSpace(gitUrl, codePath)) {
                LoggerUtil.info(log, "本地代码不存在，clone", gitUrl, codePath);
                //为了区分ssh路径和http/s这里ssh多加了一层目录
                git = instanceSshGit(gitUrl, codePath, branchName, null, privateKey);
                // 下载指定branch
                if (git.getRepository().exactRef(Constants.HEAD).isSymbolic()){
                    git.checkout().setName(branchName).call();
                }
            } else {
                LoggerUtil.info(log, "本地代码存在,直接使用", gitUrl, codePath);
                git = Git.open(new File(codePath));
                git.getRepository().getFullBranch();
                //判断是分支还是commitId，分支做更新，commitId无法改变用原有的
                if (git.getRepository().exactRef(Constants.HEAD).isSymbolic()) {
                    //更新代码
                    instanceSshGit(gitUrl, codePath, branchName, git, privateKey);
                }
            }

        } catch (GitAPIException |
                IOException e) {
            if (e instanceof GitAPIException) {
                throw new BizException(BizCode.GIT_AUTH_FAILED.getCode(), e.getMessage());
            }
            e.printStackTrace();
            throw new BizException(BizCode.GIT_OPERATED_FAIlED);
        }
        return git;
    }

    /**
     * 将代码转成树状
     *
     * @param repository
     * @param branchName
     * @return
     */
    public static AbstractTreeIterator prepareTreeParser(Repository repository, String branchName) {
        try {
            RevWalk walk = new RevWalk(repository);
            RevTree tree;
            if (null == repository.resolve(branchName)) {
                throw new BizException(BizCode.PARSE_BRANCH_ERROR);
            }
            tree = walk.parseTree(repository.resolve(branchName));
            CanonicalTreeParser treeParser = new CanonicalTreeParser();
            try (ObjectReader reader = repository.newObjectReader()) {
                treeParser.reset(reader, tree.getId());
            }
            walk.dispose();
            return treeParser;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 判断工作目录是否存在，本来可以每次拉去代码时删除再拉取，但是这样代码多的化IO比较大，所以就代码可以复用
     *
     * @param codePath
     * @return
     */
    public static Boolean checkGitWorkSpace(String gitUrl, String codePath) throws IOException {
        Boolean isExist = Boolean.FALSE;
        File repoGitDir = new File(codePath + "/.git");
        if (!repoGitDir.exists()) {
            return isExist;
        }
        Git git = Git.open(new File(codePath));
        if (null == git) {
            return isExist;
        }
        Repository repository = git.getRepository();
        //解析本地代码，获取远程uri,是否是我们需要的git远程仓库
        String repoUrl = repository.getConfig().getString("remote", "origin", "url");
        if (gitUrl.equals(repoUrl)) {
            isExist = Boolean.TRUE;
        } else {
            LoggerUtil.info(log, "本地存在其他仓的代码，先删除");
            git.close();
            FileUtils.delete(new File(codePath));
        }
        return isExist;
    }


    /**
     * 获取url类型
     *
     * @param url
     * @return
     */
    public static GitUrlTypeEnum judgeUrlType(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        if (url.toLowerCase().startsWith("http")) {
            return GitUrlTypeEnum.HTTP;
        } else if (url.toLowerCase().startsWith("git@")) {
            return GitUrlTypeEnum.SSH;
        }
        return null;
    }

    /**
     * 取远程代码本地存储路径
     *
     * @param repoUrl
     * @param localBaseRepoDir
     * @param version
     * @return
     */
    public static String getLocalDir(String repoUrl, String localBaseRepoDir, String version) {
        StringBuilder localDir = new StringBuilder(localBaseRepoDir);
        if (Strings.isNullOrEmpty(repoUrl)) {
            return "";
        }
        localDir.append("/");
        String repoName = Splitter.on("/")
                .splitToStream(repoUrl).reduce((first, second) -> second)
                .map(e -> Splitter.on(".").splitToStream(e).findFirst().get()).get();
        localDir.append(repoName);
        if (!StringUtils.isEmpty(version)) {
            localDir.append("/");
            localDir.append(version);
        }
        localDir.append("/");
        return localDir.toString();
    }

}
