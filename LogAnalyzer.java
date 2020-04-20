/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    private int[] dayCounts;
    private int[] monthlyAccesses;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer()
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
        dayCounts = new int[31];
        monthlyAccesses = new int[48];
        // Create the reader to obtain the data.
        reader = new LogfileReader();
    }
    
    public LogAnalyzer(String fileName){
        hourCounts = new int[24];
        dayCounts = new int[32];
        monthlyAccesses = new int[48];
        reader = new LogfileReader(fileName);
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }
    
    public void analyzeMonthlyData(){
        while(reader.hasNext()){
            LogEntry entry = reader.next();
            int day = entry.getDay();
            dayCounts[day - 1]++;
        }
    }
    
    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
    
    public int numberOfAccesses(){
        analyzeHourlyData();
        int total = 0;
        for(int i = 0; i < hourCounts.length; i++){
            total += hourCounts[i];
        }
        return total;
    }
    
    public int busiestHour(){
        analyzeHourlyData();
        int busiestHour = 0, busiestCount = 0;
        for(int i = 0; i < hourCounts.length; i++){
            if(hourCounts[i] >= busiestCount){
                busiestCount = hourCounts[i];
                busiestHour = i;
            }
        }
        return busiestHour;
    }
    
    public int quietestHour(){
        analyzeHourlyData();
        int quietestHour = 0, quietestCount = numberOfAccesses();
        for(int i = 0; i < hourCounts.length; i++){
            if(hourCounts[i] <= quietestCount){
                quietestCount = hourCounts[i];
                quietestHour = i;
            }
        }
        return quietestHour;
    }
    
    public int busiestTwoHours(){
        analyzeHourlyData();
        int busiestTwoHours = 0, busiestCount = 0;
        int[] hourlySums = new int[24];
        for(int i = 0; i < hourlySums.length; i++){
            if((i + 1) < (hourlySums.length - 1)){
                hourlySums[i] = hourCounts[i] + hourCounts[i+1];
            }else{
                hourlySums[i] = hourCounts[i] + hourCounts[0];
            }
        }
        
        for(int i = 0; i < hourlySums.length; i++){
            if(hourlySums[i] > busiestCount){
                busiestCount = hourlySums[i];
                busiestTwoHours = i;
            }
        }
        return busiestTwoHours;
    }
    
    public int quietestDay(){
        int quietestDayCount = 367;
        int quietestDayIndex = 0;
        for(int i = 0; i < dayCounts.length; i++){
            if(dayCounts[i] <= quietestDayCount){
                quietestDayCount = dayCounts[i];
                quietestDayIndex = i;
            }
        }
        return quietestDayIndex + 1;
    }
    
    public int busiestDay(){
        int busiestDayCount = 0;
        int busiestDayIndex = 0;
        for(int i = 0; i < dayCounts.length; i++){
            if(dayCounts[i] >= busiestDayCount){
                busiestDayCount = dayCounts[i];
                busiestDayIndex = i;
            }
        } 
        return busiestDayIndex + 1;
    }
    
    public void totalAccessesPerMonth(){
        while(reader.hasNext()){
            LogEntry entry = reader.next();
            System.out.println(entry.getMonth());
            monthlyAccesses[entry.getMonth() - 1]++;
        }
    }
    
    public int quietestMonth(){
        analyzeMonthlyData();
        int quietestMonthCount = 32;
        int quietestMonthIndex = 0;
        for(int i = 0; i < monthlyAccesses.length; i++){
            if(monthlyAccesses[i] < quietestMonthCount){
                quietestMonthCount = monthlyAccesses[i];
                quietestMonthIndex = i;
            }
        }
        return quietestMonthIndex;
    }
    
    public int busiestMonth(){
        analyzeMonthlyData();
        int busiestMonthCount = 0;
        int busiestMonthIndex = 0;
        for(int i = 0; i < monthlyAccesses.length; i++){
            if(monthlyAccesses[i] > busiestMonthCount){
                busiestMonthCount = monthlyAccesses[i];
                busiestMonthIndex = i;
            }
        }
        return busiestMonthIndex;
    }
    
    public int averageAccessesPerMonth(){
        analyzeMonthlyData();
        int monthlySum = 0;
        for(int i = 0; i < monthlyAccesses.length; i++){
            monthlySum += monthlyAccesses[i];
        }
        return monthlySum / monthlyAccesses.length;
    }
}
