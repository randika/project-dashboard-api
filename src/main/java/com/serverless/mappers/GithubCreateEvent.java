package com.serverless.mappers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A wrapper for https://developer.github.com/v3/activity/events/types/#createevent
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubCreateEvent {
    private String ref;
    private String ref_type;
    private Repository repository;
    private Sender sender;


    public String getRef() {
        return ref;
    }


    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getRef_type() {
        return ref_type;
    }
    public void setRef_type(String ref_type) {
        this.ref_type = ref_type;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }



    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repository {

        private Owner owner;
        private String name;
        private String url;

        public Owner getOwner() {
            return owner;
        }

        public void setOwner(Owner owner) {
            this.owner = owner;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Owner {

            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sender {

        private String login;
        private String avatar_url;

        public String getLogin() {
            return login;
        }
        public void setLogin(String login) {
            this.login = login;
        }

        public String getAvatar_url() {
            return avatar_url;
        }
        public void setAvatar_url(String avatar_url) {
            this.avatar_url = avatar_url;
        }
    }
}
