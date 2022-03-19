package dream.log;

public class LogFormat
{
    static final String ANSI_RESET_ALL_FLAG = "\033[0m";

    static final String ANSI_BLACK_TEXT_FLAG = "\033[0;30m";
    static final String ANSI_RED_TEXT_FLAG = "\033[0;31m";
    static final String ANSI_GREEN_TEXT_FLAG = "\033[0;32m";
    static final String ANSI_YELLOW_TEXT_FLAG = "\033[0;33m";
    static final String ANSI_BLUE_TEXT_FLAG = "\033[0;34m";
    static final String ANSI_PURPLE_TEXT_FLAG = "\033[0;35m";
    static final String ANSI_CYAN_TEXT_FLAG = "\033[0;36m";
    static final String ANSI_WHITE_TEXT_FLAG = "\033[0;37m";

    static final String ANSI_BLACK_BOLD_TEXT_FLAG = "\033[1;30m";
    static final String ANSI_RED_BOLD_TEXT_FLAG = "\033[1;31m";
    static final String ANSI_GREEN_BOLD_TEXT_FLAG = "\033[1;32m";
    static final String ANSI_YELLOW_BOLD_TEXT_FLAG = "\033[1;33m";
    static final String ANSI_BLUE_BOLD_TEXT_FLAG = "\033[1;34m";
    static final String ANSI_PURPLE_BOLD_TEXT_FLAG = "\033[1;35m";
    static final String ANSI_CYAN_BOLD_TEXT_FLAG = "\033[1;36m";
    static final String ANSI_WHITE_BOLD_TEXT_FLAG = "\033[1;37m";

    static final String ANSI_BLACK_UNDERLINED_TEXT_FLAG = "\033[4;30m";
    static final String ANSI_RED_UNDERLINED_TEXT_FLAG = "\033[4;31m";
    static final String ANSI_GREEN_UNDERLINED_TEXT_FLAG = "\033[4;32m";
    static final String ANSI_YELLOW_UNDERLINED_TEXT_FLAG = "\033[4;33m";
    static final String ANSI_BLUE_UNDERLINED_TEXT_FLAG = "\033[4;34m";
    static final String ANSI_PURPLE_UNDERLINED_TEXT_FLAG = "\033[4;35m";
    static final String ANSI_CYAN_UNDERLINED_TEXT_FLAG = "\033[4;36m";
    static final String ANSI_WHITE_UNDERLINED_TEXT_FLAG = "\033[4;37m";

    static final String ANSI_BLACK_BACKGROUND_FLAG = "\033[40m";
    static final String ANSI_RED_BACKGROUND_FLAG = "\033[41m";
    static final String ANSI_GREEN_BACKGROUND_FLAG = "\033[42m";
    static final String ANSI_YELLOW_BACKGROUND_FLAG = "\033[43m";
    static final String ANSI_BLUE_BACKGROUND_FLAG = "\033[44m";
    static final String ANSI_PURPLE_BACKGROUND_FLAG = "\033[45m";
    static final String ANSI_CYAN_BACKGROUND_FLAG = "\033[46m";
    static final String ANSI_WHITE_BACKGROUND_FLAG = "\033[47m";


    private String mainTextColor;
    private String mainTextBackgroundColor;
    private String headerTextColor;
    private String headerTextbackgroundColor;

    private final String resetFlag;

    public LogFormat()
    {
        normalize();
        resetFlag = ANSI_RESET_ALL_FLAG;
    }

    public void normalize()
    {
        mainTextColor = ANSI_WHITE_TEXT_FLAG;
        mainTextBackgroundColor = ANSI_BLACK_BACKGROUND_FLAG;

        headerTextColor = ANSI_BLACK_BOLD_TEXT_FLAG;
        headerTextbackgroundColor = ANSI_WHITE_BACKGROUND_FLAG;
    }

    public void onSuccess()
    {
        mainTextColor = ANSI_GREEN_TEXT_FLAG;
        mainTextBackgroundColor = ANSI_BLACK_BACKGROUND_FLAG;

        headerTextColor = ANSI_BLACK_BOLD_TEXT_FLAG;
        headerTextbackgroundColor = ANSI_GREEN_BACKGROUND_FLAG;
    }

    public void onError()
    {
        mainTextColor = ANSI_RED_TEXT_FLAG;
        mainTextBackgroundColor = ANSI_BLACK_BACKGROUND_FLAG;

        headerTextColor = ANSI_BLACK_BOLD_TEXT_FLAG;
        headerTextbackgroundColor = ANSI_RED_BACKGROUND_FLAG;
    }

    public void onWarning()
    {
        mainTextColor = ANSI_YELLOW_TEXT_FLAG;
        mainTextBackgroundColor = ANSI_BLACK_BACKGROUND_FLAG;

        headerTextColor = ANSI_BLACK_BOLD_TEXT_FLAG;
        headerTextbackgroundColor = ANSI_YELLOW_BACKGROUND_FLAG;
    }

    public String getResetFlag()
    {
        return this.resetFlag;
    }

    public String getMainTextColor()
    {
        return this.mainTextColor;
    }

    public void setMainTextColor(String mainTextColor)
    {
        this.mainTextColor = mainTextColor;
    }

    public String getMainTextBackgroundColor()
    {
        return this.mainTextBackgroundColor;
    }

    public void setMainTextBackgroundColor(String mainTextBackgroundColor)
    {
        this.mainTextBackgroundColor = mainTextBackgroundColor;
    }

    public String getHeaderTextColor()
    {
        return this.headerTextColor;
    }

    public void setHeaderTextColor(String headerTextColor)
    {
        this.headerTextColor = headerTextColor;
    }

    public String getHeaderTextbackgroundColor()
    {
        return this.headerTextbackgroundColor;
    }

    public void setHeaderTextbackgroundColor(String headerTextbackgroundColor)
    {
        this.headerTextbackgroundColor = headerTextbackgroundColor;
    }
}
