package src.Feature.SearchAction;

import src.Components.ImageComponent.ImageDetails;
import src.Components.Query.ImageDetailQuery;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchByCaption extends SearchManager {
    private Set<String> captionSet;
    private Set<File> imagesFromCaptionSearch;

    public Set<File> getImagesFromCaptionSearch() {
        searchImagesByCaption();
        return imagesFromCaptionSearch;
    }

    private void searchImagesByCaption() {
        captionSet = getKeywordToSearch();
        imagesFromCaptionSearch = new HashSet<>();
        for (String keyword : captionSet){
            List<ImageDetails> imageList = ImageDetailQuery.getImageListByKeyword(keyword);
            for (ImageDetails image : imageList){
                File imageFile = new File(image.getImagePath());
                imagesFromCaptionSearch.add(imageFile);
            }
        }
    }
}
