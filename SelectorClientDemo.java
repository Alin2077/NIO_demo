import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SelectorClientDemo {
    
    public static void main(String[] args) {
        new SelectorClientDemo().client();
    }

    private void client(){

        try (SocketChannel open = SocketChannel.open()) {

            InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);
            open.configureBlocking(false); //设置非阻塞
            boolean connect = open.connect(inetSocketAddress); //连接服务端

            if(!connect){
                while (!open.finishConnect()) {
                    System.out.println("连接未成功");
                }
            }

            String msg = "Hello, I'm client";
            ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());

            open.write(wrap);

        } catch (IOException e) {
            e.printStackTrace();
        }

        

    }

}
