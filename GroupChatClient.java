import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {

    private SocketChannel socketChannel;

    private Selector selector;

    private final int PORT = 6667;

    private final String IP = "127.0.0.1";

    private String userName;

    public GroupChatClient(){

        try {
            socketChannel = SocketChannel.open();
            selector = Selector.open();

            InetSocketAddress inetSocketAddress = new InetSocketAddress(IP, PORT);
            socketChannel.bind(inetSocketAddress);

            socketChannel.register(selector, SelectionKey.OP_READ);

            userName = socketChannel.getLocalAddress().toString().substring(1);
            System.out.println(userName+" is ok ");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendMsg(String msg){

        msg = userName + " say: " +msg;

        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void readMsg(){

        try {
            int select = selector.select();
            if(select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if(next.isReadable()){
                        SocketChannel channel = (SocketChannel) next.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        channel.read(allocate);
                        System.out.println(new String(allocate.array()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        
        GroupChatClient groupChatClient = new GroupChatClient();

        new Thread(() -> {
            while (true) {
                groupChatClient.readMsg();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            groupChatClient.sendMsg(msg);
        }
        scanner.close();
    }
    
}
