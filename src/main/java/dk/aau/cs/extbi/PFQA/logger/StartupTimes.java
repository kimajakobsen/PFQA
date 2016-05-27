package dk.aau.cs.extbi.PFQA.logger;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap.SimpleEntry;

public class StartupTimes {

	private long writeIndexToDiskDuration;
	private long buildIndexDuration;
	private LocalDateTime time;
	private String index;
	private String dataset;
	private long readIndexDuration;

	public StartupTimes(LocalDateTime time, SimpleEntry<String, String> dataset2, String index) {
		this.time = time;
		this.index = index;
		this.dataset = dataset2.getValue();
	}

	public void setBuildIndex(long buildIndexDuration) {
		this.buildIndexDuration = (buildIndexDuration);
	}
	
	public void setWriteIndexToDisk(long writeIndexToDiskDuration) {
        this.writeIndexToDiskDuration = (writeIndexToDiskDuration);
    }
	
	public long getBuildIndexDuration() {
        return buildIndexDuration;
    }

    public long getWriteIndexToDiskDuration() {
        return writeIndexToDiskDuration;
    }
    
    public long getUnixTimestamp() {
		ZoneId zoneId = ZoneId.systemDefault(); // or: ZoneId.of("Europe/Oslo");
		return time.atZone(zoneId).toEpochSecond();
	}
    
    public String getIndex() {
    	return index;
    }

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public void setReadIndex(long readIndexDuration) {
		this.readIndexDuration = readIndexDuration;
	}
	
	public long getReadIndex() {
		return readIndexDuration;
	}
}
