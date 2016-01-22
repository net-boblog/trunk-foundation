package com.tumbleweed.platform.trunk.base.alarm;

/**
 * Created by mylover on 1/22/16.
 */
public abstract class AbstractMessage {

    AbstractMessage() {
    }

    protected abstract String createAlarmMessage(String var1, String var2);

    public static enum AlarmLevel {
        FATAL {
            public int getValue() {
                return 0;
            }
        },
        CRITICAL {
            public int getValue() {
                return 1;
            }
        },
        IMPORTANT {
            public int getValue() {
                return 2;
            }
        },
        NORMAL {
            public int getValue() {
                return 3;
            }
        },
        OTHER {
            public int getValue() {
                return 4;
            }
        },
        DEVLOPE {
            public int getValue() {
                return 5;
            }
        },
        NOTIFY {
            public int getValue() {
                return 6;
            }
        },
        DEBUG {
            public int getValue() {
                return 7;
            }
        };

        private AlarmLevel() {
        }

        public abstract int getValue();
    }

    public static enum AlarmType {
        BuildAlarm {
            public int getValue() {
                return 0;
            }
        },
        CancelAlarm {
            public int getValue() {
                return 1;
            }
        };

        private AlarmType() {
        }

        public abstract int getValue();
    }

    public static enum MsgType {
        AlarmMsg {
            public int getValue() {
                return 0;
            }
        },
        HeartBeat {
            public int getValue() {
                return 1;
            }
        },
        ChangedAPI {
            public int getValue() {
                return 2;
            }
        };

        private MsgType() {
        }

        public abstract int getValue();
    }
}
