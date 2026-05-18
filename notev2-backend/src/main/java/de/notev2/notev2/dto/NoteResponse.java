package de.notev2.notev2.dto;

public class NoteResponse {
        private Long id;
        private String title;
        private String content;
    

    
        public NoteResponse() {
           
        }

        public Long getId() {
            return id;
        }
    
        public void setId(Long id) {
            this.id = id;
        }
    
        public String getTitle() {
            return title;
        }
    
        public void setTitle(String title) {
            this.title = title;
        }
    
        public String getContent() {
            return content;
        }
    
        public void setContent(String content) {
            this.content = content;
        }
}
