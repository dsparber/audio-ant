use audioantTest;

-- Show results
SELECT
	s.name, 
	t.testDate, 
	r.fileName, 
	r.correctRecognition, 
	f.strongestFrequency 
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
	f.strongestFrequency 
FROM 
	FeatureMatch as f, 
	Results as r, 
	Sounds as s, 
	Tests as t 
WHERE 
	t.id = r.testId AND 
	s.id = r.soundId AND 
	f.resultId = r.id AND
	r.CorrectRecognition = 0;
	
-- Show test summary
SET sql_mode = '';
SELECT
	t.testDate as 'Date',
	t.fileType,
	COUNT(DISTINCT r.soundId) as 'Number of sounds',
	COUNT(*) / COUNT(DISTINCT r.soundId) as 'Files per Sound',
	AVG(r.correctRecognition) as 'Avg. correctness'
FROM
	Results as r, 
	Sounds as s, 
	Tests as t 
WHERE 
	t.id = r.testId AND 
	s.id = r.soundId
GROUP BY
	Date;