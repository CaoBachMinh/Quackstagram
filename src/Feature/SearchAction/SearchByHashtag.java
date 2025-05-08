package src.Feature.SearchAction;

import src.Components.UIComponents.ImageDetails;
import src.Components.Query.ImageDetailQuery;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class SearchByHashtag extends SearchManager {
    private Set<String> hashtagSet;
    private Set<File> imagesFromHashtagSearch;

    public Set<File> getImagesFromHashtagSearch(){
        searchImagesByHashtag();
        return imagesFromHashtagSearch;
    }
    
    private void searchImagesByHashtag(){
        hashtagSet = getHashtagToSearch();
        imagesFromHashtagSearch = new HashSet<>();
        for (String hashtag : hashtagSet){ // O(m) m small
            List<ImageDetails> imagelist = ImageDetailQuery.getImageListByHashtag(hashtag); //O(1)
            for(ImageDetails image : imagelist) { // O(n)
                File newImageFile = new File(image.getImagePath());
                imagesFromHashtagSearch.add(newImageFile);   // O(1)
            }
        }
    }
}
