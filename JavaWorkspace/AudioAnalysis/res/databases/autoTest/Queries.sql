use audioantTest;

-- Show results
SELECT
	t.id,
	s.name, 
	t.testDate, 
	r.fileName, 
	r.correctRecognition, 
	r.shouldBeRecognised,
	f.strongestFrequency,
	f.spectralRolloffPoint,
	f.mfcc,
	f.energy
FROM 
	FeatureMatch as f, 
	Results as r, 
	Sounds as s, 
	Tests as t 
WHERE 
	t.id = r.testId AND 
	s.id = r.soundId AND 
	f.resultId = r.id;
	
-- Show wrong detections
SELECT
	t.id,
	t.testDate, 
	s.name, 
	r.fileName,  
	f.strongestFrequency,
	f.spectralRolloffPoint,
	f.mfcc,
	f.energy
FROM 
	FeatureMatch as f, 
	Results as r, 
	Sounds as s, 
	Tests as t 
WHERE 
	t.id = r.testId AND 
	s.id = r.soundId AND 
	f.resultId = r.id AND
	r.correctRecognition = 0;
	
-- Show test summary
SET sql_mode = '';
SELECT
	t.id,
	t.testDate as 'Date',
	t.fileType,
	COUNT(DISTINCT r.soundId) as 'Number of sounds',
	COUNT(*) / COUNT(DISTINCT r.soundId) as 'Files per Sound',
	AVG(r.correctRecognition) as 'Avg. correctness'
FROM
	Results as r, 
	Tests as t 
WHERE 
	t.id = r.testId
GROUP BY
	Date;

-- CSV export
SELECT 
	r.shouldBeRecognised,  
	f.strongestFrequency, 
	f.spectralRolloffPoint,
	f.mfcc,
	f.energy
FROM  
	FeatureMatch as f,  
	Results as r,  
	Tests as t  
WHERE  
	t.id = r.testId AND 
	f.resultId = r.id AND
	t.id = 119
INTO 
	OUTFILE '/tmp/databaseExport.csv'
	FIELDS ENCLOSED BY '"' TERMINATED BY ';' ESCAPED BY '"'
	LINES TERMINATED BY '\r\n';