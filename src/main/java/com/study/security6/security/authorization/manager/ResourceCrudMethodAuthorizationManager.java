package com.study.security6.security.authorization.manager;

import com.study.security6.security.authorization.method.CrudMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.parameters.P;

abstract public class ResourceCrudMethodAuthorizationManager {
    private static AuthorizationDecision GRANT = new AuthorizationDecision(true);
    private static AuthorizationDecision DENY = new AuthorizationDecision(false);

    public static AuthorizationDecision getDecision(boolean isGranted){
        if(isGranted){
            return GRANT;
        }else {
            return DENY;
        }
    }

    protected AuthorizationDecision delegateAuthorize(CrudMethod method, Long resourceId){
        if(method == CrudMethod.CREATE){
            return  create(resourceId);
        }else if(method == CrudMethod.READ){
            return read(resourceId);
        }else if(method == CrudMethod.UPDATE){
            return update(resourceId);
        }else if(method == CrudMethod.DELETE){
            return delete(resourceId);
        }else {
            return DENY;
        }
    }

    protected AuthorizationDecision read(Long resourceId){
        return GRANT;
    }

    protected AuthorizationDecision create(Long resourceId){
        return DENY;
    }

    protected AuthorizationDecision delete(Long resourceId){
        return DENY;
    }

    protected AuthorizationDecision update(Long resourceId){
        return DENY;
    }

}
