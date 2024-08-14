package com.study.security6.security.authorization.method.board.manager;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.security.authentication.AuthenticationSupplier;
import com.study.security6.security.authorization.manager.BoardAuthorizeManager;
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
public class BoardMethodAuthorizationManager {

    private final BoardAuthorizeManager boardAuthorizeManager;

    private static AuthorizationDecision DENY = new AuthorizationDecision(false);

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

        AuthorizationDecision decision = DENY;
        if(crud == CrudMethod.READ){
          decision = read();
        } else if(crud == CrudMethod.DELETE){
            decision = delete(boardId);
        }else if(crud == CrudMethod.CREATE){
            decision = create();
        }else if(crud == CrudMethod.UPDATE){
            decision = update(boardId);
        }

        if(decision.isGranted())
            return joinPoint.proceed();
        else
            throw new AccessDeniedException("access denied");
    }

    /**
     * 읽는 건 모두에게 허용
     * @return
     */
    private AuthorizationDecision read(){
        return new AuthorizationDecision(true);
    }

    /**
     * create는 Admin에게만 허용
     * @return
     */
    private AuthorizationDecision create(){
        return boardAuthorizeManager.checkAdmin();
    }

    /**
     * 삭제는 boardManager에게 허용
     * @param boardId
     * @return
     */
    private AuthorizationDecision delete(Long boardId){
        return boardAuthorizeManager.checkBoardManager(boardId);
    }

    /**
     * Update도 board Manager에게 허용
     * @param boardId
     * @return
     */
    private AuthorizationDecision update(Long boardId){
        return boardAuthorizeManager.checkBoardManager(boardId);    }
}
