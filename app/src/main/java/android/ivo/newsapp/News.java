package android.ivo.newsapp;

class News {
    /**
     * The date when the article has been published
     */
    private String publicationDate;
    /**
     * The type of news (e.g. Politics, Business, Environment)
     */
    private String sectionName;
    /**
     * The title of the news article
     */
    private String title;
    /**
     * Link to the public news address
     */
    private String httpUrl;
    /**
     * The byline on a newspaper or magazine article gives the name of the writer of the article
     */
    private String byline;

    static class Builder {
        private String publicationDate;
        private String sectionName;
        private String title;
        private String httpUrl;
        private String byline;

        News build() {
            return new News(this);
        }

        Builder byline(String val) {
            byline = val;
            return this;
        }

        Builder publicationDate(String val) {
            publicationDate = val;
            return this;
        }

        Builder sectionName(String val) {
            sectionName = val;
            return this;
        }

        Builder title(String val) {
            title = val;
            return this;
        }

        Builder apiUrl(String val) {
            httpUrl = val;
            return this;
        }
    }

    private News(Builder builder) {
        publicationDate = builder.publicationDate;
        sectionName = builder.sectionName;
        title = builder.title;
        httpUrl = builder.httpUrl;
        byline = builder.byline;
    }

    String getPublicationDate() {
        String result = publicationDate;
        if (result != null) {
            result = result
                    .replace("T", " at ")
                    .replace("Z", "");
        }
        return result;
    }

    String getByline() {
        return byline;
    }

    String getSectionName() {
        return sectionName;
    }

    String getTitle() {
        return title;
    }

    String getHttpUrl() {
        return httpUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "publicationDate='" + publicationDate + '\'' +
                ", sectionName='" + sectionName + '\'' +
                ", title='" + title + '\'' +
                ", httpUrl='" + httpUrl + '\'' +
                ", byline='" + byline + '\'' +
                '}';
    }
}
