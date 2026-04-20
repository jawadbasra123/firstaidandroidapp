package edu.northeastern.NUMAD26Sp_FirstAidEmergency;

import java.util.List;

public class GeminiModels {
    public static class Request {
        public List<Content> contents;
        public Request(List<Content> contents) { this.contents = contents; }
    }

    public static class Content {
        public String role; // "user" or "model"
        public List<Part> parts;
        
        public Content(List<Part> parts) { 
            this.parts = parts; 
        }
        
        public Content(String role, List<Part> parts) {
            this.role = role;
            this.parts = parts;
        }
    }

    public static class Part {
        public String text;
        public Part(String text) { this.text = text; }
    }

    public static class Response {
        public List<Candidate> candidates;
    }

    public static class Candidate {
        public Content content;
    }
}