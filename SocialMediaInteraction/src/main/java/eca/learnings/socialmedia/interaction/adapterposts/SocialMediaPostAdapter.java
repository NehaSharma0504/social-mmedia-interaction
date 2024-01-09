package eca.learnings.socialmedia.interaction.adapterposts;


import eca.learnings.socialmedia.interaction.model.SocialMediaPost;

import java.util.ArrayList;
import java.util.List;

// Adapter to convert ExternalSocialMediaAPI into a common format
//SocialMediaDataProvider -- client expects
class SocialMediaPostAdapter implements SocialMediaDataProvider {
    private ExternalSocialMediaAPI externalAPI;

    public SocialMediaPostAdapter(ExternalSocialMediaAPI externalAPI) {
        this.externalAPI = externalAPI;
    }

    @Override
    public List<SocialMediaPost> getPosts() {
        List<String> postData = externalAPI.getPosts();
        // Convert the external post data to a list of SocialMediaPost objects
        List<SocialMediaPost> posts = new ArrayList<>();
        for (String data : postData) {
            // Parse the data and create SocialMediaPost objects
            // Add them to the list
        }
        return posts;
    }
}
