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
	f.resultId = r.id AND
	CorrectRecognition = 0;