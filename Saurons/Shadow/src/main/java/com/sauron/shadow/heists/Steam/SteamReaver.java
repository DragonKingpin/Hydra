package com.sauron.shadow.heists.Steam;

import com.sauron.radium.heistron.*;
import org.jsoup.select.Elements;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

public class SteamReaver extends MegaDOMIndexCrew implements Reaver {
    public SteamReaver(HTTPIndexHeist heist, int id ){
        super( heist, id );
    }

    @Override
    protected Page afterPageFetched( Page page, Request request ){
        String[] cookieGroup = {
                "wants_mature_content=1; steamCountry=US%7C6200f47c9b62892472b38bf7bbfd9a20; browserid=3066434021590310219; sessionid=d3260d1c80b4ff080f3c3641; timezoneOffset=28800,0; _ga=GA1.2.650079156.1685022988; _gid=GA1.2.697747378.1685022988; ak_bmsc=2CA539E4F2635E8FB79CF2F744843296~000000000000000000000000000000~YAAQXY0duFuJ2y+IAQAABpQyUxPg33L/2piDSim4d/G5+YQl4fuLirFFGlXtPSRxbh3xoU0Ohnb8FyHO30d/nuLiVKiOV2X6drabWJZ1UjnodRMJLWqKYWPNZjaKf1ZLQGkHflTxg5qaAAz+dS389vPGWWM53jZvD8ZZbYsOucK3oWJoRL+I7nJhwS6k0+JZVEckl3Al3V7gvx4shiDTHmTZ/z8+dTnpFpf/fWRCWVFWRWExB/VWDNDFJInXrVTEIwcBQ4wSWRFkJfW4d/S5JQl2QSDHyqaHpgW1va2vAICYX/GKFR/lxrgXVm2LBLT6UqFv1BSx/UzJfsZZm2mCxktV2FKaASRYt3pUcfGVBXlNrZ2LCtj72mNrJ78FKD/50SWSNS72; steamLoginSecure=76561199447520905%7C%7CeyAidHlwIjogIkpXVCIsICJhbGciOiAiRWREU0EiIH0.eyAiaXNzIjogInI6MEQzNV8yMjk2RTFFM19BQ0JDMSIsICJzdWIiOiAiNzY1NjExOTk0NDc1MjA5MDUiLCAiYXVkIjogWyAid2ViIiBdLCAiZXhwIjogMTY4NTExMDc5MSwgIm5iZiI6IDE2NzYzODMwMDMsICJpYXQiOiAxNjg1MDIzMDAzLCAianRpIjogIjBEMzFfMjI5NkUxREZfQUQ3MDQiLCAib2F0IjogMTY4NTAyMzAwMiwgInJ0X2V4cCI6IDE3MDI4OTc1NjIsICJwZXIiOiAwLCAiaXBfc3ViamVjdCI6ICI3NC4xMjEuMTg4LjIyMSIsICJpcF9jb25maXJtZXIiOiAiNzQuMTIxLjE4OC4yMjEiIH0.hzq-8liTaMgNVPoLOzeFmmjRIiSgjMwhsFYlBrFEC37Q3QSQ6sC1xbSYY3tLlh9DL5VUDfF05bA59M03sx_8Bg; recentapps=%7B%22981160%22%3A1685023036%7D; birthtime=28828801; lastagecheckage=1-0-1964",
        };
        Elements age = page.getHtml().getDocument().select("#app_agegate");
        Elements login = page.getHtml().getDocument().select("#error_box");
        if( age.size()==0&&login.size()==0 ) {
            return page;
        }
        else{
            for( int i = 0; i < cookieGroup.length; ++i ){
                Page retryPage = this.queryHTTPPageSafe( new Request(page.getRequest().getUrl()).addHeader("cookie",cookieGroup[i]) );
                if( retryPage.getHtml().getDocument().select("#app_agegate").size() == 0 ){
                    return retryPage;
                }
            }
            return page;
        }
    }

    @Override
    public String querySpoilStoragePath( long id ) {
        return this.querySpoilStorageDir( id ) + "page_" + id + ".html";
    }


    @Override
    public void toRavage() {
        this.startBatchTask();
    }
}
