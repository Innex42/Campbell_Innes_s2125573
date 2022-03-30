package org.me.gcu.campbell_innes_s2125573;

public class ItemClass {

        private String title;
        private String description;
        private String link;
        private String geoPoint;
        private String date;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) { this.title = title; }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) { this.link = link; }

        public String getGeoPoint() {
            return geoPoint;
        }

        public void setGeoPoint(String geoPoint) { this.geoPoint = geoPoint; }

        public String getDate() {
            return date;
        }

        public void setDate(String date) { this.date = date; }

        @Override
        public String toString() {

            String temp;

            temp = "title='" + title + '\'' +
                    ", description='" + description + '\'' +
                    ", link='" + link + '\'' +
                    ", geoPoint='" + geoPoint + '\'' +
                    ", date='" + date + '\'' +
                    '}';
            return temp;
        }
    }


