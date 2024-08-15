package com.study.security6.security.authorization.method.comment.manager;

import com.study.security6.domain.board.dto.BoardDto;
import com.study.security6.domain.board.service.BoardService;
import com.study.security6.domain.comment.repository.CommentRepository;
import com.study.security6.domain.comment.service.CommentService;
import com.study.security6.domain.role.board.dto.BoardRoleDto;
import com.study.security6.domain.role.board.service.BoardRoleService;
import com.study.security6.security.authentication.AuthenticationSupplier;
import com.study.security6.security.authorization.manager.BoardAuthorizeManager;
import com.study.security6.security.authorization.manager.ResourceCrudMethodAuthorizationManager;
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
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Aspect
@Component
public class CommentMethodAuthorizationManager extends ResourceCrudMethodAuthorizationManager {
    private final CommentRepository commentRepository;
    private final BoardAuthorizeManager boardAuthorizeManager;
    private final AuthenticationSupplier authenticationSupplier;

    @Autowired
    public CommentMethodAuthorizationManager(CommentRepository commentRepository, BoardAuthorizeManager boardAuthorizeManager) {
        this.commentRepository = commentRepository;
        this.boardAuthorizeManager = boardAuthorizeManager;
        this.authenticationSupplier = AuthenticationSupplier.getInstance();
    }

    @Around("@annotation(com.study.security6.security.authorization.method.comment.annotation.CommentPreAuthorize)")
    public Object authorize(ProceedingJoinPoint joinPoint) throws Throwable {
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

        AuthorizationDecision decision = delegateAuthorize(crud, commentId);
        if(decision.isGranted()){
            return joinPoint.proceed();
        }else {
            throw  new AccessDeniedException("DENY");
        }
    }

    @Transactional(readOnly = true)
    protected CommentAuthorizeDto findCommentByCommentId(Long commentId){
        return commentRepository.findCommntAOByCommentId(commentId);
    }

    private boolean isResourceOwner(CommentAuthorizeDto comment){
        Long userId = (Long) authenticationSupplier.get().getPrincipal();
        if(userId == comment.getUserId()){
            return true;
        }else {
            return false;
        }
    }

    /**
     *
     * @param boardId 예외적으로 댓글 작성할때는 "댓글 ID"라는 것이 존재하지 않으므로 boardId로 처리함
     * @return
     */
    @Override
    protected AuthorizationDecision create(Long boardId) {
        if(!boardAuthorizeManager.checkAuthenticate().isGranted()){
            // 인증되지 않은 사용자인 경우
            return  ResourceCrudMethodAuthorizationManager.getDecision(false);
        }
        // 사용자가 해당 Board에서 금지되었는 지 확인
        return boardAuthorizeManager.checkBoardBanned(boardId);
    }

    @Override
    protected AuthorizationDecision delete(Long commentId) {
        CommentAuthorizeDto comment = findCommentByCommentId(commentId);
        if(isResourceOwner(comment)){
            return ResourceCrudMethodAuthorizationManager.getDecision(true);
        }
        return boardAuthorizeManager.checkBoardManager(comment.getBoardId());
    }

    @Override
    protected AuthorizationDecision update(Long commentId) {
        CommentAuthorizeDto comment = findCommentByCommentId(commentId);
        return ResourceCrudMethodAuthorizationManager.getDecision(
                isResourceOwner(comment)
        );
    }
}
