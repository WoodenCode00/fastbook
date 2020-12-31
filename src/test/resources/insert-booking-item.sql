-- Insert test data
INSERT INTO booking_item (id, title, description, address, base_daily_cost, checkin_time, checkout_time)
VALUES (
 REPLACE('a42d22e0-42fb-11eb-b378-0242ac130002', '-', ''), 
 'Camp site on new born island', 
 'Unique experience to live on the only campsite on new born island', 
 'Paradise Street, 1, Paradise Island, Pacific Ocean', 
 499.99, 
 '00:01',
 '00:00');
