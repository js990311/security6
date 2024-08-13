package com.study.security6.security.authorization.method.manager;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.security.authentication.AuthenticationSupplier;
import com.study.security6.security.authorization.method.annotation.BoardAuthorization;
import com.study.security6.security.authorization.method.annotation.CrudMethod;
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
    
    private final BoardService boardService;
    private final BoardRoleService boardRoleService;
    private final Map<Long, AuthorizationManager> delegates;
    private final AuthorityAuthorizationManager<Object> adminAuthorizationManager;
    private final AuthenticationSupplier authenticationSupplier;

    private static AuthorizationDecision DENY = new AuthorizationDecision(false);

    @Autowired
    public BoardMethodAuthorizationManager(BoardService boardService, BoardRoleService boardRoleService) {
        this.boardService = boardService;
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

    @Around("@annotation(com.study.security6.security.authorization.method.annotation.BoardAuthorization)")
    public Object parseTest(ProceedingJoinPoint joinPoint) throws Throwable {
        Authentication authentication = authenticationSupplier.get();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        BoardAuthorization annotation = method.getAnnotation(BoardAuthorization.class);
        String boardIdparamName = annotation.boardId();
        CrudMethod crud = annotation.method();
        
        // 읽기는 별도의 Role을 필요로 하지 않는다 
        if(crud == CrudMethod.READ){
            return joinPoint.proceed();
        }

        if(authentication == null || !authentication.isAuthenticated()){
            throw new AccessDeniedException("access denied");
        }
        
        Long boardId = -1l;
        String[] params = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        for(int i=0;i<params.length;i++){
            if(boardIdparamName.equals(params[i])){
                boardId = (Long) args[i];
            }
        }

        AuthorizationDecision decision = DENY;
        if(crud == CrudMethod.DELETE){
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

    private AuthorizationDecision create(){
        return adminAuthorizationManager.check(authenticationSupplier, null);
    }

    private AuthorizationDecision delete(Long boardId){
        if(boardId == -1){
            // 정체를 알 수 없는 접근
            return DENY;
        }
        BoardDto board = boardService.readBoard(boardId);
        if(delegates.containsKey(boardId)){
            return delegates.get(boardId).check(authenticationSupplier, null);
        }else {
            return DENY;
        }
    }

    private AuthorizationDecision update(Long boardId){
        if(boardId == -1){
            // 정체를 알 수 없는 접근
            return DENY;
        }
        BoardDto board = boardService.readBoard(boardId);
        if(delegates.containsKey(boardId)){
            return delegates.get(boardId).check(authenticationSupplier, null);
        }else {
            return DENY;
        }
    }
}
