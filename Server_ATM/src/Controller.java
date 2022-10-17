import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller extends Thread {

    private final Socket clientSocket;
    private final DataInputStream input;
    private final DataOutput output;

    private int id = -1;

    public Controller(Socket socket) {
        this.clientSocket = socket;
        try {
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public synchronized void start() {
        super.start();
        while (true) {
            try {
                int id = Integer.parseInt(input.readUTF());
                boolean exists = DummyData.listOFIDS.contains(id);
                if (exists){
                    this.id = id;
                }
                output.writeBoolean(exists);

            } catch (IOException e) {
//                throw new RuntimeException(e);
                System.out.println("Client " + clientSocket.getInetAddress() + " disconnected");
                return;
            } catch (NumberFormatException nf) {
                nf.printStackTrace();
            }

        }
    }
}
