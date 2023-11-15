import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多人聊天室服务端
 */
public class GroupChatServer {
    
    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private final int PORT = 6667;

    private final String IP = "127.0.0.1";

    public GroupChatServer(){

        //初始化属性
        try{
            this.serverSocketChannel = ServerSocketChannel.open();
            this.selector = Selector.open();
            
            InetSocketAddress address = new InetSocketAddress(IP, PORT);
            this.serverSocketChannel.bind(address);

            this.serverSocketChannel.configureBlocking(false);
            this.serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void listen(){

        try {
            while (true) {

                int select = selector.select(2000);

                if(select <= 0 ){
                    System.out.println("wait for new msg...");
                }else{
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectedKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey next = iterator.next();
                        if(next.isAcceptable()){
                            SocketChannel accept = serverSocketChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector, SelectionKey.OP_READ);
                            System.out.println(accept.getRemoteAddress()+" is online...");
                        }else if(next.isReadable()){
                            read(next);
                        }
                        iterator.remove();
                    }
                }

            }

        } catch (Exception e) {
           e.printStackTrace();
        }

    }

    private void read(SelectionKey selectionKey){
        SocketChannel channel = null;

        try {
            channel = (SocketChannel) selectionKey.channel();
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            int read = channel.read(allocate);
            if(read > 0){
                String msg = new String(allocate.array());
                System.out.println("from "+channel.getRemoteAddress()+" : "+msg);
                notityAllClient(msg,channel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void notityAllClient(String msg,SocketChannel channel) throws IOException{

        System.out.println("server forward msg");
        for(SelectionKey key : selector.keys()){
            SelectableChannel channel2 = key.channel();
            if(channel2 instanceof SocketChannel && channel2 != channel){
                SocketChannel socketChannel = (SocketChannel) channel2;
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                socketChannel.write(wrap);
            }
        }

    }

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
