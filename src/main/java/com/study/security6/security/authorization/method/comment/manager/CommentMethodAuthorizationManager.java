package com.study.security6.security.authorization.method.comment.manager;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.comment.service.CommentService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.security.authentication.AuthenticationSupplier;
import com.study.security6.security.authorization.method.CrudMethod;
import com.study.security6.security.authorization.method.board.annotation.BoardPreAuthorize;
import com.study.security6.security.authorization.method.comment.annotation.CommentPreAuthorize;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CommentMethodAuthorizationManager {
    private final CommentService commentService;
    private final BoardRoleService boardRoleService;
    private final Map<Long, AuthorizationManager> delegates;
    private final AuthorityAuthorizationManager<Object> adminAuthorizationManager;
    private final AuthenticationSupplier authenticationSupplier;
    private static AuthorizationDecision DENY = new AuthorizationDecision(false);

    @Autowired
    public CommentMethodAuthorizationManager(CommentService commentService, BoardRoleService boardRoleService) {
        this.commentService = commentService;
        this.boardRoleService = boardRoleService;
        this.adminAuthorizationManager = AuthorityAuthorizationManager.hasRole("ADMIN");

        List<BoardRoleDto> boardRoles = boardRoleService.readAllBoardRole();
        authenticationSupplier = new AuthenticationSupplier();
        delegates = new HashMap<>();
        for(BoardRoleDto boardRole : boardRoles){
            delegates.put(boardRole.getBoardId(),
                    AuthorityAuthorizationManager.hasRole(boardRole.getRoleName())
            );
        }
    }

    @Around("@annotation(com.study.security6.security.authorization.method.comment.annotation.CommentPreAuthorize)")
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = authenticationSupplier.get();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CommentPreAuthorize annotation = method.getAnnotation(CommentPreAuthorize.class);
        CrudMethod crud = annotation.method();

        String commentIdName = annotation.commentId();
        Long commentId = -1l;
        String[] params = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for(int i=0;i<params.length;i++){
            if(commentIdName.equals(params[i])){
                commentId = (Long) args[i];
            }
        }

        return joinPoint.proceed();
    }

    private AuthorizationDecision create(){
        return DENY;
    }

    private AuthorizationDecision delete(Long commentId){
        return DENY;
    }

    private AuthorizationDecision update(Long commentId){
        return DENY;
    }

}
