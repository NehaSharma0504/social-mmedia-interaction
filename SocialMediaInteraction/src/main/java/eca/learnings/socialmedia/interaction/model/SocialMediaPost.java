package eca.learnings.socialmedia.interaction.model;

import lombok.Getter;
import lombok.Setter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.lang.NonNull;

import java.util.List;

@Getter
@Setter
public class SocialMediaPost {
    @Id
    private ObjectId id;
    private String title;
    private String content;
    private String author;
    private List<String> tags;
    @NonNull
    private String accountId;
    @NonNull
    private String authorId;

    public SocialMediaPost() {
        // Default no-argument constructor required by the MongoDB Java driver.
    }


    //Builder Design Pattern
    private SocialMediaPost(PostBuilder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.content = builder.content;
        this.author = builder.author;
        this.tags = builder.tags;
        this.accountId = builder.accountId;
        this.authorId = builder.authorId;
    }

    public static class PostBuilder {
        private ObjectId id;
        private String title;
        private String content;
        private String author;
        private List<String> tags;
        @NonNull
        private String accountId;
        @NonNull
        private String authorId;

        public PostBuilder(ObjectId id, String title, String content) {
            this.id = id;
            this.title = title;
            this.content = content;
        }

        public PostBuilder author(String accountId, String author, String authorId) {
            this.accountId = accountId;
            this.author = author;
            this.authorId = authorId;
            return this;
        }

        public PostBuilder tags(List<String> tags) {
            this.tags = tags;
            return this;
        }

        public SocialMediaPost build() {
            return new SocialMediaPost(this);
        }
    }
    public synchronized Document toBson() {
        return new Document()
                .append("id", id)
                .append("author", author)
                .append("content", content)
                .append("tags", tags)
                .append("title", title)
                .append("accountId", accountId)
                .append("authorId", authorId);
    }
}

