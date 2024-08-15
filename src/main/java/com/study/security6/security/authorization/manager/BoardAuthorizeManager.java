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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BoardAuthorizeManager {
    private final BoardService boardService;
    private final BoardRoleService boardRoleService;
    private Map<Long, AuthorizationManager> boardManagerRoles;
    private Map<Long, AuthorizationManager> boardBannedRoles;
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
        List<BoardRoleDto> boardManagers = boardRoleService.findBoardRoleByisBanned(false);
        this.boardManagerRoles = new HashMap<>();
        for(BoardRoleDto manager : boardManagers){
            this.boardManagerRoles.put(manager.getBoardId(),
                    AuthorityAuthorizationManager.hasRole(manager.getRoleName())
            );
        }

        List<BoardRoleDto> boardBanneds = boardRoleService.findBoardRoleByisBanned(true);
        this.boardBannedRoles = new HashMap<>();
        for(BoardRoleDto banned : boardBanneds){
            this.boardBannedRoles.put(banned.getBoardId(),
                    AuthorityAuthorizationManager.hasRole(banned.getRoleName())
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
        if(!checkAuthenticate().isGranted()){
            return DENY;
        }
        return adminAuthorizationManager.check(authenticationSupplier, authenticationSupplier.get().getAuthorities());
    }

    public AuthorizationDecision checkBoardManager(Long boardId){
        if(!checkAuthenticate().isGranted()){
            return DENY;
        }
        if(!boardManagerRoles.containsKey(boardId)){
            return DENY;
        }
        return boardManagerRoles.get(boardId).check(authenticationSupplier, authenticationSupplier.get().getAuthorities());
    }

    public AuthorizationDecision checkBoardBanned(Long boardId){
        if(!checkAuthenticate().isGranted()){
            // 인증되지 않은 사용자니까 차단 내역이 없음
            return GRANT;
        }
        if(!boardBannedRoles.containsKey(boardId)){
            // 해당 Board에는 Banned된 Role이 없음
            return GRANT;
        }
        if(boardBannedRoles.get(boardId).check(authenticationSupplier, authenticationSupplier.get().getAuthorities()).isGranted()){
            // BannedRole을 가지고 있는 경우 AuthorizationManager가 Grant하도록 구현되어있다. 실제로는 DENY 해야함
            return DENY;
        }else {
            return GRANT;
        }
    }
}
