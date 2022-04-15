package org.me.gcu.campbell_innes_s2125573;

import java.util.Date;

public class ItemClass {

        private String title="";
        private String description="";
        private Date startDate;
        private Date endDate;
        private String link="";
        private String geoPoint="";
        private Float latitude;
        private Float longitude;
        private String pubDate="";

        public ItemClass(){
            this.title = title;
            this.description = description;
            this.startDate = startDate;
            this.endDate = endDate;
            this.link = link;
            this.geoPoint = geoPoint;
            this.latitude = latitude;
            this.longitude = longitude;
            this.pubDate = pubDate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) { this.title = title; }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) { this.description = description; }

        public Date getStartDate(){return startDate;}

        public void setStartDate(Date startDate){this.startDate = startDate;}

        public Date getEndDate(){return endDate;}

        public void setEndDate(Date endDate){this.endDate = endDate;}

        public String getLink() { return link; }

        public void setLink(String link) { this.link = link; }

        public String getGeoPoint() {
            return geoPoint;
        }

        public void setGeoPoint(String geoPoint) { this.geoPoint = geoPoint; }

        public Float getLatitude(){ return latitude; }

        public void setLatitude(Float latitude){ this.latitude = latitude; }

        public Float getLongitude(){ return longitude; }

        public void setLongitude(Float longitude){ this.longitude = longitude; }

        public String getPubDate() {
            return pubDate;
        }

        public void setPubDate(String pubDate) { this.pubDate = pubDate; }

        @Override
        public String toString() {

            String temp;

            temp = "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", link='" + link + '\'' +
                    ", startDate='" + startDate + '\'' +
                    ", endDate='" + endDate + '\'' +
                    ", geoPoint='" + geoPoint + '\'' +
                    ", pubDate='" + pubDate + '\'' +
                    ", latitude='" + latitude + '\'' +
                    ", longitude='" + longitude + '\''
                    ;
            return temp;
        }
    }


