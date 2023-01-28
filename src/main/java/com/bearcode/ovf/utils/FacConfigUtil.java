package com.bearcode.ovf.utils;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.FlowType;

public class FacConfigUtil {

    private static final String SKIMM = "skimm";
    private static final String VOTE411 = "vote411";
    private static final String US_VOTE = "usvote";
    private static final String DEFAULT = "default";

    public static boolean isS3StorageRequired(FaceConfig config) {
        return config != null && (config.getRelativePrefix().contains(SKIMM) || config.getRelativePrefix().contains(VOTE411) || config.getRelativePrefix().contains(US_VOTE));
    }

    public static String getFaceName(String urlPath) {
        if (urlPath == null) {
            return DEFAULT;
        } else if (urlPath.contains(SKIMM)) {
            return SKIMM;
        } else if (urlPath.contains(VOTE411)) {
            return VOTE411;
        } else if (urlPath.contains(US_VOTE)) {
            return US_VOTE;
        } else {
            return DEFAULT;
        }
    }

    public static boolean isSkimm(String path){
        return path != null && path.contains(SKIMM);
    }

    public static boolean isVote411(String path){
        return path != null && path.contains(VOTE411);
    }
}
