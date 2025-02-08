package com.sauron.heist.heistron;

public final class Heists {
    public static String getCriterionNomenclatureName( Heistum heistum ) {
        String szHeistName;
        if( heistum instanceof CascadeHeist ) {
            szHeistName = ((CascadeHeist) heistum).getInstanceFullName();
        }
        else {
            szHeistName = heistum.heistName();
        }

        return szHeistName;
    }
}
