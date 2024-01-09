package eca.learnings.userdata;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Getter
@Setter
@NoArgsConstructor //for JSON serialization/deserialization
@Document(collection = "Users")
public class User {
        @Id
        private String id;
        private String userId;
        private String userName;
        private String email;

        public org.bson.Document toBson() {
                return new org.bson.Document()
                        .append("userId", userId)
                        .append("userName", userName)
                        .append("email", email);
        }
}
