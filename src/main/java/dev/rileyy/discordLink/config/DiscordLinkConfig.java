package dev.rileyy.discordLink.config;


public class DiscordLinkConfig {

    public String getJWT_SIGNING_KEY() {
        return JWT_SIGNING_KEY;
    }

    public void setJWT_SIGNING_KEY(String JWT_SIGNING_KEY) {
        this.JWT_SIGNING_KEY = JWT_SIGNING_KEY;
    }

    public String getBEARER_TYPE() {
        return BEARER_TYPE;
    }

    public void setBEARER_TYPE(String BEARER_TYPE) {
        this.BEARER_TYPE = BEARER_TYPE;
    }

    public String getSERVER_URL() {
        return SERVER_URL;
    }

    public void setSERVER_URL(String SERVER_URL) {
        this.SERVER_URL = SERVER_URL;
    }

    public String getPORT() {
        return PORT;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public String JWT_SIGNING_KEY = "";

    public String BEARER_TYPE = "Bearer";

    public String SERVER_URL = "0.0.0.0";

    public String PORT = "5418";
}
