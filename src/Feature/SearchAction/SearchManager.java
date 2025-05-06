package src.Feature.SearchAction;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SearchManager {
    
    private String textToSearch;
    private static Set<String> keywordSet;
    private static Set<String> hashtagSet;
    private static String usernameToSearch;
    private Set<File> imagesToDisplay;
    private Set<String> usernameToDisplay;


    public SearchManager(String textToSearch){
        this.textToSearch = textToSearch;
    }

    protected  SearchManager(){ //default constructor for children class
        this.textToSearch = null;
    }


    public  File[] getImageToDisplay(){
        File[] imageData = imagesToDisplay.toArray(new File[imagesToDisplay.size()]);
        return imageData;
    }

    public Set<String> getUserToDisplay(){
        return usernameToDisplay;
    }

    public void processSearch(){
        TextHandler textHandler = new TextHandler(textToSearch);
        textHandler.processText();

        this.classifyAndMergeImages();
        this.addUserToDisplay();
    }


    protected Set<String> getKeywordToSearch(){
        return keywordSet;
    }

    protected Set<String> getHashtagToSearch(){
        return hashtagSet;
    }

    protected String usernameToSearch(){
        return usernameToSearch;
    } 

    private void addUserToDisplay(){
        usernameToDisplay = new HashSet<>();

        if (usernameToSearch != null && !usernameToSearch.isEmpty()) {
            SearchByUsername searchByUsername = new SearchByUsername();
            usernameToDisplay = searchByUsername.getMatchingUsernames();
        }
    }

    
    private void classifyAndMergeImages(){
        Set<File> imagesFromPostSearch = null;
        Set<File> imagesFromHashtagSearch = null;

        if (keywordSet.isEmpty() && hashtagSet.isEmpty()){
            imagesToDisplay = null; // Or set as empty? 
        }

        if(!keywordSet.isEmpty()) {
             SearchByCaption searchByCaption = new SearchByCaption();
             imagesFromPostSearch = searchByCaption.getImagesFromCaptionSearch();
         }
        if(!hashtagSet.isEmpty()) {
            SearchByHashtag searchByHashtag = new SearchByHashtag();
            imagesFromHashtagSearch = searchByHashtag.getImagesFromHashtagSearch();
        }
        mergeImageToDisplay(imagesFromPostSearch,imagesFromHashtagSearch);
    }

    private void mergeImageToDisplay(Set<File> imagesFromPostSearch, Set<File> imagesFromHashtagSearch) {
        imagesToDisplay = new HashSet<>();

        if (imagesFromPostSearch != null) {
            for (File file : imagesFromPostSearch){
                if (file != null)
                    imagesToDisplay.add(file);
            }
        }

        if (imagesFromHashtagSearch != null) {
            for (File file : imagesFromHashtagSearch){
                if (file != null)
                    imagesToDisplay.add(file);
            }
        }
    }

    private class InvalidInputException extends RuntimeException {
        public InvalidInputException(String message) {
            super(message);
        }
    }

    private class TextHandler{
        private String textToProcess;
        TextHandler (String textToProcess){
            this.textToProcess = textToProcess;
        }

        void processText(){
            try{
                String cleanedText = cleanText();
                classifyText(cleanedText);
            }catch (InvalidInputException e) {
                System.err.println(e.getMessage());
            }
        }

        String cleanText() {
            if (textToProcess.matches(".*[\\p{Punct}&&[^#._]].*")) {
                throw new InvalidInputException("Input text can NOT contain special character");
            }
        
                if (textToProcess == null) {
                    throw new InvalidInputException("Input text can NOT be null");
                }
                // Trim leading and trailing whitespace
                String cleanedText = textToProcess.trim();
                // Handle empty input or whitespace-only input
                if (cleanedText.isEmpty()) {
                    throw new InvalidInputException("Input text can NOT be empty");
                }
            
                // Normalize multiple spaces between words
                cleanedText = cleanedText.replaceAll("\\s+", " ");
            
                System.out.println(cleanedText);
                return cleanedText;
            }


        void classifyText (String cleanedText){
                String[] words = cleanedText.split("\\s+");
                keywordSet = new HashSet<>();
                hashtagSet = new HashSet<>();
                usernameToSearch=null;
        
                if (words.length == 1 && !words[0].startsWith("#") ) {
                    usernameToSearch = cleanUsername(words[0]);
                }

                for (String word : words) {
                    if (isSkipWord(word)) continue;
                    if (word.startsWith("#")) {
                        word = cleanHashtag(word);
                        hashtagSet.add(word);
                    } else {
                        word = cleanKeyWord(word); 
                        if(word.isEmpty()) continue;
                        keywordSet.add(word);
                    }
                }

            }
        String cleanUsername(String username){
            return username.replaceAll("[#]", "").toLowerCase().trim();
        }

        String cleanKeyWord(String word) {
            return word.replaceAll("[._#]", "").toLowerCase().trim();
        }

        String cleanHashtag(String word) {
            return word.replaceAll("[^#a-zA-Z0-9]", "").trim();
        }

        boolean isSkipWord(String word) {
            return word.isEmpty() || word.matches("\\.+") || word.matches("_+");
        }
    }
}
