package eca.learnings.userdata.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor //for JSON serialization/deserialization
@Document(collection = "Users")
public class User {
        @Id
        private String _id;
        private String userId;
        private String userName;
        private String email;
        private HashMap<String, List<String>> followersMap; //key will be social media platform name and value will be user ids


        public org.bson.Document toBson() {
                return new org.bson.Document()
                        .append("userId", userId)
                        .append("userName", userName)
                        .append("email", email)
                        .append("followersMap", followersMap);
        }
        public static User fromDocument(org.bson.Document document) {
                User user = new User();
                user.set_id(document.getObjectId("_id").toString());
                user.setUserId(document.getString("userId"));
                user.setUserName(document.getString("userName"));
                user.setEmail(document.getString("email"));
                // Map other fields in a similar way

                return user;
        }
}
