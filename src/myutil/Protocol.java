package myutil;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketException;

/**
 * ·â×°Ò»¸öÐ­Òé
 *
 * @author Administrator
 */
public class Protocol {
    public static int TYPE_REGISTER = 0;//×¢²á
    public static int TYPE_IMAGE = 1;//Í¼Æ¬
    public static int TYPE_LOAD = 2;//µÇÂ¼
    public static int TYPE_LOGOUT = 3;//ÍË³ö
    public static int TYPE_CONNECT_FAILED =4;//µÇÂ¼Ê§°Ü


    public static void send(int type, byte[] bytes, DataOutputStream dos) {
        int totalLen = 1 + 4 + bytes.length;
        try {
            dos.writeByte(type);
            dos.writeInt(totalLen);
            dos.write(bytes);
            dos.flush();
        } catch (IOException e) {
            System.exit(0);
        }

    }

    public static Result getResult(DataInputStream dis) {
        try {
            byte type = dis.readByte();
            int totalLen = dis.readInt();
            byte[] bytes = new byte[totalLen - 4 - 1];
            dis.readFully(bytes);
            return new Result(type & 0xFF, totalLen, bytes);
        } catch (IOException e) {
            System.out.println(e);
            return new Result(TYPE_LOGOUT,1,null);
        }
    }
}