package com.study.security6.security.authorization.manager;

import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.security.authentication.AuthenticationSupplier;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authorization.AuthorityAuthorizationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BoardAuthorizeManager {
    private final BoardService boardService;
    private final BoardRoleService boardRoleService;
    private Map<Long, AuthorizationManager> delegates;
    private final AuthorityAuthorizationManager<Object> adminAuthorizationManager;
    private final AuthenticationSupplier authenticationSupplier;

    private static AuthorizationDecision GRANT = ResourceCrudMethodAuthorizationManager.getDecision(true);
    private static AuthorizationDecision DENY = ResourceCrudMethodAuthorizationManager.getDecision(false);

    public BoardAuthorizeManager(BoardService boardService, BoardRoleService boardRoleService) {
        this.boardService = boardService;
        this.boardRoleService = boardRoleService;
        this.adminAuthorizationManager = AuthorityAuthorizationManager.hasRole("ADMIN");
        this.authenticationSupplier = AuthenticationSupplier.getInstance();
        buildAuthorizeManager();
    }

    private void buildAuthorizeManager(){
        List<BoardRoleDto> boardRoles = boardRoleService.readAllBoardRole();
        delegates = new HashMap<>();
        for(BoardRoleDto boardRole : boardRoles){
            delegates.put(boardRole.getBoardId(),
                    AuthorityAuthorizationManager.hasRole(boardRole.getRoleName())
            );
        }
    }

    public AuthorizationDecision checkAuthenticate(){
        Authentication authentication = authenticationSupplier.get();
        if(authentication == null || !authentication.isAuthenticated()){
            return DENY;
        }else {
            return GRANT;
        }
    }

    public AuthorizationDecision checkAdmin(){
        if(checkAuthenticate().isGranted()){
            return DENY;
        }

        return adminAuthorizationManager.check(authenticationSupplier, authenticationSupplier.get().getAuthorities());
    }

    public AuthorizationDecision checkBoardManager(Long boardId){
        if(checkAuthenticate().isGranted()){
            return DENY;
        }
        if(!delegates.containsKey(boardId)){
            return DENY;
        }
        return delegates.get(boardId).check(authenticationSupplier, authenticationSupplier.get().getAuthorities());
    }
}
