package src;

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


    public  Set<File> getImageToDisplay(){
        return imagesToDisplay;
    }

    public  Set<String> getUserToDisplay(){
        return usernameToDisplay;
    }

    public void processSearch(){
        TextHandler textHandler = new TextHandler(textToSearch);
        textHandler.processText();

        // this.classifyAndMergeImages();
        // this.usernameToDisplay = //Add Method search Username của Long ở đây
    }


    protected Set<String> getKeywordToSearch(){
        return keywordSet;
    }

    protected Set<String> getHashtagToSearch(){
        return hashtagSet;
    }

    protected String usernameToSearch(){
        return  usernameToSearch;
    } 


    private void classifyAndMergeImages(){
        if (keywordSet.isEmpty() && hashtagSet.isEmpty()){
            imagesToDisplay = null; // Or set as empty? 
        }
        // else if(!keywordSet.isEmpty()) {
        //     Set<File> imagesFromPostSearch = //implement Post search method
        // }
        else if(!hashtagSet.isEmpty()) {
            SearchByHashtag searchByHashtag = new SearchByHashtag();
            Set<File> imagesFromHashtagSearch = searchByHashtag.getImagesFromHashtagSearch();
        }
        // mergeImageToDisplay(imagesFromPostSearch,imagesFromHashtagSearch);
    }

    private void mergeImageToDisplay(Set<File> imagesFromPostSearch, Set<File> imagesFromHashtagSearch) {
        imagesToDisplay = new HashSet<>();

        for (File file : imagesFromPostSearch){
            if (file != null) 
                imagesToDisplay.add(file);
        }
        for (File file : imagesFromHashtagSearch){
            if (file != null) 
                imagesToDisplay.add(file);
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
            String cleanedText = cleanText();
            classifyText(cleanedText);
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
        
                if (words.length == 1 && !words[0].startsWith("#") ) {
                    usernameToSearch = cleanWord(words[0]); 
                }

                for (String word : words) {
                    if (isSkipWord(word)) continue;
                    if (word.startsWith("#")) {
                        word = cleanHashtag(word);
                        hashtagSet.add(word);
                    } else {
                        word = cleanWord(word); 
                        if(word.isEmpty()) continue;
                        keywordSet.add(word);
                    }
                }

            }
        String cleanWord(String word) {
                return word.replaceAll("^[._#]+|[._#]+$", "").toLowerCase().trim();
            }

        String cleanHashtag(String word) {
                return word.replaceAll("[^#a-zA-Z0-9]", "").trim();
            }

        boolean isSkipWord(String word) {
                return word.isEmpty() || word.matches("\\.+") || word.matches("_+");
            }
    }
}


