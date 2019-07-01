package client.model;

@SuppressWarnings("squid:ClassVariableVisibilityCheck")
public class Config {
    public final int[] port;
    public final String ARG_DELIMITER;
    public final String CMD_DELIMITER;
    public final int connection_type;
    public final int view_type;

    public Config(int[] port, String ARG_DELIMITER, String CMD_DELIMITER, int connection_type,int view_type){
        this.port=port;
        this.ARG_DELIMITER=ARG_DELIMITER;
        this.CMD_DELIMITER=CMD_DELIMITER;
        this.connection_type=connection_type;
        this.view_type=view_type;
    }
}
