
package ruchad.codepath.nytsearch.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Response {

    @SerializedName("docs")
    @Expose
    private List<ruchad.codepath.nytsearch.models.Doc> docs = new ArrayList<ruchad.codepath.nytsearch.models.Doc>();

    /**
     * 
     * @return
     *     The docs
     */
    public List<ruchad.codepath.nytsearch.models.Doc> getDocs() {
        return docs;
    }

    /**
     * 
     * @param docs
     *     The docs
     */
    public void setDocs(List<ruchad.codepath.nytsearch.models.Doc> docs) {
        this.docs = docs;
    }

}
