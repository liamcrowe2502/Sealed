package com.liam.sealed.modelClass;

    public class message {
        String message;
        String senderid;
        long timeStamp;

        public message() {
        }

        public message(String message, String senderid, long timeStamp) {
            this.message = message;
            this.senderid = senderid;
            this.timeStamp = timeStamp;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSenderid() {
            return senderid;
        }

        public void setSenderid(String senderid) {
            this.senderid = senderid;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(long timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
