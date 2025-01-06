-- Create composite index for location-based queries
CREATE INDEX IF NOT EXISTS idx_alert_location ON alerts (latitude, longitude);

