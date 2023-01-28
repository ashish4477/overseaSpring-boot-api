rem Call this after doing clean, init, migrate with flyway

pushd data
mysql -u overseas --password=v0t!n9_4ud overseas_foundation < all.sql
popd
