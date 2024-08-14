package com.study.security6.security.authorization.method.board.manager;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.security.authentication.AuthenticationSupplier;
import com.study.security6.security.authorization.manager.BoardAuthorizeManager;
import com.study.security6.security.authorization.manager.ResourceCrudMethodAuthorizationManager;
import com.study.security6.security.authorization.method.board.annotation.BoardPreAuthorize;
import com.study.security6.security.authorization.method.CrudMethod;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorityAuthorizationDecision;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class BoardMethodAuthorizationManager extends ResourceCrudMethodAuthorizationManager {
    private final BoardAuthorizeManager boardAuthorizeManager;

    @Autowired
    public BoardMethodAuthorizationManager(BoardAuthorizeManager boardAuthorizeManager) {
        this.boardAuthorizeManager = boardAuthorizeManager;
    }

    @Around("@annotation(com.study.security6.security.authorization.method.board.annotation.BoardPreAuthorize)")
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BoardPreAuthorize annotation = method.getAnnotation(BoardPreAuthorize.class);
        String boardIdparamName = annotation.boardId();
        CrudMethod crud = annotation.method();

        // Authorization 관련 정보 추출
        Long boardId = -1l;
        String[] params = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for(int i=0;i<params.length;i++){
            if(boardIdparamName.equals(params[i])){
                boardId = (Long) args[i];
            }
        }

        AuthorizationDecision decision = delegateAuthorize(crud, boardId);
        if(decision.isGranted())
            return joinPoint.proceed();
        else
            throw new AccessDeniedException("access denied");
    }


    /**
     * create는 Admin에게만 허용
     * @return
     */
    protected AuthorizationDecision create(Long boardId){
        return boardAuthorizeManager.checkAdmin();
    }

    /**
     * 삭제는 boardManager에게 허용
     * @param boardId
     * @return
     */
    protected AuthorizationDecision delete(Long boardId){
        return boardAuthorizeManager.checkBoardManager(boardId);
    }

    /**
     * Update도 board Manager에게 허용
     * @param boardId
     * @return
     */
    protected AuthorizationDecision update(Long boardId){
        return boardAuthorizeManager.checkBoardManager(boardId);    }
}
