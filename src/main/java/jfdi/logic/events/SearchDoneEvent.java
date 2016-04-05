// @@author A0130195M
package jfdi.logic.events;

import jfdi.storage.apis.TaskAttributes;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * @author Liu Xinan
 */
public class SearchDoneEvent {

    private ArrayList<TaskAttributes> results;
    private HashSet<String> keywords;

    public SearchDoneEvent(ArrayList<TaskAttributes> results, HashSet<String> keywords) {
        this.results = results;
        this.keywords = keywords;
    }

    public ArrayList<TaskAttributes> getResults() {
        return results;
    }

    public HashSet<String> getKeywords() {
        return keywords;
    }

}
