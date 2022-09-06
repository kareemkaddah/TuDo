package de.ls5.wt2.authentication;

import java.util.List;

public class TokenBlockList {
    private List<String> blockedTokens;


    public TokenBlockList(List<String> blockedTokens) {
        this.blockedTokens = blockedTokens;
    }

    public void refresh(){
        for (String token: blockedTokens) {
            if (!JWTUtil.validateToken(token)) {
                blockedTokens.remove(token);
            }
        }
    }

    public boolean isBlockedToken(String token){
        this.refresh();
        for (String t: blockedTokens) {
            if (t.equals(token)) {
               return true;
            }
        }
        return false;
    }

    public boolean add(String token){
        if (isBlockedToken(token)){
            return false;
        }
        this.blockedTokens.add(token);
        return false;
    }

    public void clear(){
        blockedTokens.clear();
    }
}
