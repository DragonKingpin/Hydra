package com.pinecone.hydra.servgram;

public enum ActionType {
    Sequential          ("Sequential"),
    Parallel            ("Parallel"),
    Loop                ("Loop"),

    SequentialActions   ("SequentialActions"),
    ParallelActions     ("ParallelActions"),
    LoopActions         ("LoopActions"),

    Break               ("Break"),
    Continue            ("Continue"),
    Jump                ("Jump"),;



    private final String value;

    ActionType( String value ){
        this.value = value;
    }

    public String getName(){
        return this.value;
    }

    public boolean isActionGroup() {
        return this == ActionType.SequentialActions || this == ActionType.ParallelActions || this == ActionType.LoopActions;
    }

    public ActionType reinterpretActions() {
        switch ( this ) {
            case LoopActions: {
                return ActionType.Loop;
            }
            case ParallelActions: {
                return ActionType.Parallel;
            }
            case SequentialActions: {
                return ActionType.Sequential;
            }
            default: {
                return this;
            }
        }
    }

    public static String queryName( ActionType type ) {
        return type.getName();
    }

    public static ActionType queryActionType( String sz ) {
        return ActionType.valueOf( sz );
    }

    public static final String  ConfigActionTypeKey  = "Type"  ;
}
