import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class ChannelDemo2 {

    public static void main(String[] args) {
        
        

        try (Scanner scanner = new Scanner(System.in);
            SocketChannel open = SocketChannel.open();){

            open.configureBlocking(false); //设置为非阻塞模式
            open.connect(new InetSocketAddress("127.0.0.1", 6666));

            while (!open.finishConnect()) {
                System.out.println("please wait for connect");
            }

            System.out.println("input your message:");
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(1024); //在内存上直接定义一个字节缓存区
            while (true) {
                String nextLine = scanner.nextLine();

                if(nextLine.isEmpty()){
                    System.out.println("you input a empty string,please try again");
                    continue;
                }

                if(nextLine.equals("exit")){
                    System.out.println("exit success");
                    break;
                }

                ByteBuffer wrap = allocateDirect.wrap(nextLine.getBytes());

                if(wrap.hasRemaining()){
                    open.write(wrap);
                }            

            }

            allocateDirect.clear();

        }catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
}
