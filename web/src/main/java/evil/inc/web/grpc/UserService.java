package evil.inc.web.grpc;

import com.google.protobuf.Empty;
import evil.inc.web.httpclient.DeclarativeUsersClient;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends UserServiceGrpc.UserServiceImplBase {

    private final DeclarativeUsersClient declarativeUsersClient;

    public UserService(DeclarativeUsersClient declarativeUsersClient) {
        super();
        this.declarativeUsersClient = declarativeUsersClient;
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<Users> responseObserver) {
        List<User> users = declarativeUsersClient.getUsers()
                .stream()
                .map(u -> User.newBuilder().setUsername(u.username()).setId(u.id()).setName(u.name()).build())
                .toList();

        responseObserver.onNext(Users.newBuilder().addAllUsers(users).build());
        responseObserver.onCompleted();
    }

}
