package com.bearcode.ovf.webservices.localelections;

import com.bearcode.ovf.webservices.localelections.model.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Test for LocalElectionService
 *
 * Created by leonid on 06.06.16.
 */
public class LocalElectionsServiceTest extends EasyMockSupport {

    private LocalElectionsService localElectionsService;

    private final static String ELECTIONS = "{\n" +
            "    \"meta\": {\n" +
            "        \"offset\": 0,\n" +
            "        \"limit\": 10,\n" +
            "        \"total_objects\": 3,\n" +
            "        \"next\": null,\n" +
            "        \"previous\": null\n" +
            "    },\n" +
            "    \"objects\": [\n" +
            "        {\n" +
            "            \"id\": 1,\n" +
            "            \"created_at\": \"2016-05-24T15:57:56.103974Z\",\n" +
            "            \"updated_at\": \"2016-05-24T15:57:56.104002Z\",\n" +
            "            \"title\": \"Boston City Election\",\n" +
            "            \"contact_email\": \"admins@bear-code.com\",\n" +
            "            \"contact_phone\": \"\",\n" +
            "            \"state\": {\n" +
            "                \"short_name\": \"AL\",\n" +
            "                \"name\": \"Alabama\",\n" +
            "                \"id\": \"S01\"\n" +
            "            },\n" +
            "            \"election_level\": {\n" +
            "                \"id\": 3,\n" +
            "                \"name\": \"City Council\"\n" +
            "            },\n" +
            "            \"election_type\": {\n" +
            "                \"id\": 2,\n" +
            "                \"name\": \"General\"\n" +
            "            },\n" +
            "            \"election_status\": \"pending\",\n" +
            "            \"election_day_registration_is_available\": true,\n" +
            "            \"use_overseas_dates_as_military_dates\": false,\n" +
            "            \"election_date\": \"2016-05-09\",\n" +
            "            \"is_public\": true,\n" +
            "            \"additional_information\": \"\",\n" +
            "            \"locations\": [\n" +
            "                {\n" +
            "                    \"name\": \"Autauga County\",\n" +
            "                    \"type\": \"county\",\n" +
            "                    \"id\": \"C01001\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"urls\": [],\n" +
            "            \"dates\": [\n" +
            "                {\n" +
            "                    \"id\": 1,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-05-01\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun May 01, 2016\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"short_url\": \"http://localelections.usvotefoundation.org/e/1/\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 2,\n" +
            "            \"created_at\": \"2016-05-27T16:38:24.104471Z\",\n" +
            "            \"updated_at\": \"2016-05-27T17:23:05.196047Z\",\n" +
            "            \"title\": \"Alaska State Primary Election\",\n" +
            "            \"contact_email\": \"info@usvotefoundation.org\",\n" +
            "            \"contact_phone\": \"(202)470-2480\",\n" +
            "            \"state\": {\n" +
            "                \"short_name\": \"AK\",\n" +
            "                \"name\": \"Alaska\",\n" +
            "                \"id\": \"S02\"\n" +
            "            },\n" +
            "            \"election_level\": {\n" +
            "                \"id\": 1,\n" +
            "                \"name\": \"State\"\n" +
            "            },\n" +
            "            \"election_type\": {\n" +
            "                \"id\": 2,\n" +
            "                \"name\": \"General\"\n" +
            "            },\n" +
            "            \"election_status\": \"approved\",\n" +
            "            \"election_day_registration_is_available\": false,\n" +
            "            \"use_overseas_dates_as_military_dates\": true,\n" +
            "            \"election_date\": \"2016-08-16\",\n" +
            "            \"is_public\": true,\n" +
            "            \"additional_information\": \"\",\n" +
            "            \"locations\": [],\n" +
            "            \"urls\": [],\n" +
            "            \"dates\": [\n" +
            "                {\n" +
            "                    \"id\": 5,\n" +
            "                    \"kind\": \"DBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 10,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"post received by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-08-06\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Post received by Sat August 06, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 6,\n" +
            "                    \"kind\": \"DBED\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-08-16\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Tue August 16, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 3,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": \"09:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Sun July 17, 2016 09:00AM US/Alaska if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 4,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 7,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"online\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun July 17, 2016 if Online\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 2,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Sun July 17, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 7,\n" +
            "                    \"kind\": \"EVF\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-08-01\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Mon August 01, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 8,\n" +
            "                    \"kind\": \"EVT\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-08-16\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Tue August 16, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 15,\n" +
            "                    \"kind\": \"OBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-15\",\n" +
            "                    \"time\": \"17:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Fri July 15, 2016 05:00PM US/Alaska if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 14,\n" +
            "                    \"kind\": \"OBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 6,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"mail\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-15\",\n" +
            "                    \"time\": \"17:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Fri July 15, 2016 05:00PM US/Alaska for ballot by mail\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 13,\n" +
            "                    \"kind\": \"OBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 7,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"online\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-15\",\n" +
            "                    \"time\": \"17:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Fri July 15, 2016 05:00PM US/Alaska if Online\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 12,\n" +
            "                    \"kind\": \"OBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 10,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"post received by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Post received by Sun July 17, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 18,\n" +
            "                    \"kind\": \"OBED\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-16\",\n" +
            "                    \"time\": \"20:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Sat July 16, 2016 08:00PM US/Alaska if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 17,\n" +
            "                    \"kind\": \"OBED\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 7,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"online\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-16\",\n" +
            "                    \"time\": \"20:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Sat July 16, 2016 08:00PM US/Alaska if Online\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 16,\n" +
            "                    \"kind\": \"OBED\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-16\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Sat July 16, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 11,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun July 17, 2016 if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 10,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 7,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"online\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun July 17, 2016 if Online\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 9,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-07-17\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Sun July 17, 2016\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"short_url\": \"http://localelections.usvotefoundation.org/e/2/\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"id\": 3,\n" +
            "            \"created_at\": \"2016-05-27T17:17:44.023817Z\",\n" +
            "            \"updated_at\": \"2016-05-27T17:28:36.243188Z\",\n" +
            "            \"title\": \"Alaska General Election\",\n" +
            "            \"contact_email\": \"info@usvotefoundation.org\",\n" +
            "            \"contact_phone\": \"(123)123123123\",\n" +
            "            \"state\": {\n" +
            "                \"short_name\": \"AK\",\n" +
            "                \"name\": \"Alaska\",\n" +
            "                \"id\": \"S02\"\n" +
            "            },\n" +
            "            \"election_level\": {\n" +
            "                \"id\": 1,\n" +
            "                \"name\": \"State\"\n" +
            "            },\n" +
            "            \"election_type\": {\n" +
            "                \"id\": 2,\n" +
            "                \"name\": \"General\"\n" +
            "            },\n" +
            "            \"election_status\": \"approved\",\n" +
            "            \"election_day_registration_is_available\": false,\n" +
            "            \"use_overseas_dates_as_military_dates\": true,\n" +
            "            \"election_date\": \"2016-11-08\",\n" +
            "            \"is_public\": true,\n" +
            "            \"additional_information\": \"\",\n" +
            "            \"locations\": [],\n" +
            "            \"urls\": [],\n" +
            "            \"dates\": [\n" +
            "                {\n" +
            "                    \"id\": 23,\n" +
            "                    \"kind\": \"DBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 10,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"post received by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-29\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Post received by Sat October 29, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 24,\n" +
            "                    \"kind\": \"DBED\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-08\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Sat October 08, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 35,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-11-07\",\n" +
            "                    \"time\": \"17:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Mon November 07, 2016 05:00PM US/Alaska\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 21,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun October 09, 2016 if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 34,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-11-07\",\n" +
            "                    \"time\": \"17:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Mon November 07, 2016 05:00PM US/Alaska if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 20,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 6,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"mail\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun October 09, 2016 for ballot by mail\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 33,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 7,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"online\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-11-07\",\n" +
            "                    \"time\": \"17:00:00\",\n" +
            "                    \"time_zone\": \"US/Alaska\",\n" +
            "                    \"time_zone_offset\": \"-0800\",\n" +
            "                    \"date_human_readable\": \"Mon November 07, 2016 05:00PM US/Alaska if Online\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 19,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Sun October 09, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 22,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 10,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"post received by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Post received by Sun October 09, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 32,\n" +
            "                    \"kind\": \"DRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 10,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"post received by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-29\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Post received by Sat October 29, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 25,\n" +
            "                    \"kind\": \"EVF\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-24\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Mon October 24, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 26,\n" +
            "                    \"kind\": \"EVT\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-11-08\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Tue November 08, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 31,\n" +
            "                    \"kind\": \"OBRD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 10,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"post received by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-29\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Post received by Sat October 29, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 27,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"default\": true,\n" +
            "                        \"name\": \"blank (no label)\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-08-16\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Tue August 16, 2016\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 30,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"fax\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun October 09, 2016 if by fax\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 29,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 6,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"mail\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Sun October 09, 2016 for ballot by mail\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 28,\n" +
            "                    \"kind\": \"ORD\",\n" +
            "                    \"date_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"default\": false,\n" +
            "                        \"name\": \"postmarked by\"\n" +
            "                    },\n" +
            "                    \"date\": \"2016-10-09\",\n" +
            "                    \"time\": null,\n" +
            "                    \"time_zone\": null,\n" +
            "                    \"time_zone_offset\": null,\n" +
            "                    \"date_human_readable\": \"Postmarked by Sun October 09, 2016\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"short_url\": \"http://localelections.usvotefoundation.org/e/3/\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    private static final String STATES = "{\n" +
            "  \"meta\": {\n" +
            "    \"offset\": 0,\n" +
            "    \"limit\": 10,\n" +
            "    \"total_objects\": 56,\n" +
            "    \"next\": \"http://localelections.usvotefoundation.org/api/v1/states?limit=10&offset=10\",\n" +
            "    \"previous\": null\n" +
            "  },\n" +
            "  \"objects\": [\n" +
            "    {\n" +
            "      \"id\": \"S01\",\n" +
            "      \"name\": \"Alabama\",\n" +
            "      \"short_name\": \"AL\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S02\",\n" +
            "      \"name\": \"Alaska\",\n" +
            "      \"short_name\": \"AK\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S04\",\n" +
            "      \"name\": \"Arizona\",\n" +
            "      \"short_name\": \"AZ\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S05\",\n" +
            "      \"name\": \"Arkansas\",\n" +
            "      \"short_name\": \"AR\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S06\",\n" +
            "      \"name\": \"California\",\n" +
            "      \"short_name\": \"CA\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S08\",\n" +
            "      \"name\": \"Colorado\",\n" +
            "      \"short_name\": \"CO\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S09\",\n" +
            "      \"name\": \"Connecticut\",\n" +
            "      \"short_name\": \"CT\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S10\",\n" +
            "      \"name\": \"Delaware\",\n" +
            "      \"short_name\": \"DE\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S11\",\n" +
            "      \"name\": \"District of Columbia\",\n" +
            "      \"short_name\": \"DC\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": \"S12\",\n" +
            "      \"name\": \"Florida\",\n" +
            "      \"short_name\": \"FL\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    private final static String STATE_VOTER_INFORMATION = "{\n" +
            "    \"meta\": {\n" +
            "        \"offset\": 0,\n" +
            "        \"limit\": 10,\n" +
            "        \"total_objects\": 1,\n" +
            "        \"next\": null,\n" +
            "        \"previous\": null\n" +
            "    },\n" +
            "    \"objects\": [\n" +
            "        {\n" +
            "            \"id\": 113,\n" +
            "            \"state\": {\n" +
            "                \"name\": \"Alabama\",\n" +
            "                \"short_name\": \"AL\",\n" +
            "                \"id\": \"S01\"\n" +
            "            },\n" +
            "            \"eligibility_general_info\": \"\",\n" +
            "            \"voting_general_info\": null,\n" +
            "            \"voter_id_general_info\": \"this is some voter_id_general_info\",\n" +
            "            \"eligibility_requirements\": [\n" +
            "                {\n" +
            "                    \"id\": 271,\n" +
            "                    \"voter_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"D\",\n" +
            "                        \"name\": \"Domestic Voter\"\n" +
            "                    },\n" +
            "                    \"header\": \"You are eligible to vote in Alabama if you:\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 0,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 68,\n" +
            "                                \"kind\": \"at_least_18_yo\",\n" +
            "                                \"name\": \"Are at least 18 years old by Election Day\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 59,\n" +
            "                                \"kind\": \"state_resident\",\n" +
            "                                \"name\": \"Are a resident of Alabama\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 272,\n" +
            "                    \"voter_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"D\",\n" +
            "                        \"name\": \"Domestic Voter\"\n" +
            "                    },\n" +
            "                    \"header\": \"You are NOT eligible to vote in Alabama if:\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 1,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 72,\n" +
            "                                \"kind\": \"mentally_incompetent\",\n" +
            "                                \"name\": \"You have been legally declared \\\"mentally incompetent\\\" by a court\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 273,\n" +
            "                    \"voter_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"D\",\n" +
            "                        \"name\": \"Domestic Voter\"\n" +
            "                    },\n" +
            "                    \"header\": \"Restorative Requirements\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"Some Footer Text\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 2,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 78,\n" +
            "                                \"kind\": \"previously_convicted_of_felony_crime\",\n" +
            "                                \"name\": \"If you have been previously convicted of a felony crime, you may vote in Alabama if your voting rights have been restored\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            ],\n" +
            "            \"identification_requirements\": [\n" +
            "                {\n" +
            "                    \"id\": 59,\n" +
            "                    \"category\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"voter_registration\",\n" +
            "                        \"name\": \"Voter Registration\",\n" +
            "                        \"name_format\": \"\"\n" +
            "                    },\n" +
            "                    \"header\": \"To register to vote in Alabama you should provide one of the following:\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 0,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 42,\n" +
            "                                \"kind\": \"your_state_name_id_number\",\n" +
            "                                \"name\": \"Your Alabama ID Number\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 10,\n" +
            "                                \"kind\": \"your_state_name_drivers_license_number\",\n" +
            "                                \"name\": \"Your Alabama Driver's License Number\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 60,\n" +
            "                    \"category\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"voter_registration\",\n" +
            "                        \"name\": \"Voter Registration\",\n" +
            "                        \"name_format\": \"\"\n" +
            "                    },\n" +
            "                    \"header\": \"If you do not have these IDs, you may provide:\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 1,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 79,\n" +
            "                                \"kind\": \"the_last_four_digits_of_your_social_security_number\",\n" +
            "                                \"name\": \"The Last Four Digits of your Social Security Number\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 188,\n" +
            "                    \"category\": {\n" +
            "                        \"id\": 2,\n" +
            "                        \"kind\": \"voting_in_person\",\n" +
            "                        \"name\": \"Voting In-Person\",\n" +
            "                        \"name_format\": \"\"\n" +
            "                    },\n" +
            "                    \"header\": \"In Alabama, you need to show a valid ID to vote. You can use any ID from this list:\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 0,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 136,\n" +
            "                                \"kind\": \"valid_state_name_issued_id\",\n" +
            "                                \"name\": \"Valid Alabama Issued ID\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 209,\n" +
            "                                \"kind\": \"hunting_or_fishing_id\",\n" +
            "                                \"name\": \"Hunting or Fishing ID\"\n" +
            "                            },\n" +
            "                            \"position\": 1\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 241,\n" +
            "                    \"category\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"kind\": \"voting_overseas\",\n" +
            "                        \"name\": \"Voting Overseas\",\n" +
            "                        \"name_format\": \"Voting Overseas formatted\"\n" +
            "                    },\n" +
            "                    \"header\": \"U.S. citizens living overseas may register and request a ballot using form (\\\"FPCA\\\").\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"Some Footer Text\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 0,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 243,\n" +
            "                                \"kind\": \"last_4_digits_of_your_social_security_number\",\n" +
            "                                \"name\": \"Last 4 Digits of your Social Security Number\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 246,\n" +
            "                                \"kind\": \"option_to_indicate_that_you_do_not_have_the_requested_id\",\n" +
            "                                \"name\": \"Option to Indicate that you do not have the Requested ID\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 245,\n" +
            "                                \"kind\": \"us_state_or_territory_or_district_issued_id\",\n" +
            "                                \"name\": \"U.S. State or Territory or District Issued ID\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 297,\n" +
            "                    \"category\": {\n" +
            "                        \"id\": 4,\n" +
            "                        \"kind\": \"voting_military\",\n" +
            "                        \"name\": \"Voting Military\",\n" +
            "                        \"name_format\": \"\"\n" +
            "                    },\n" +
            "                    \"header\": \"U.S. citizens living overseas may register and request a ballot using the overseas voter registration/ballot request form. You will have the following identification options when completing the form:\nService members and their dependents may register and request a ballot using the federal voter registration/ballot request form (\\\"FPCA\\\"). You will have the following identification options when completing the form:\",\n" +
            "                    \"header_type\": \"M\",\n" +
            "                    \"footer\": \"\",\n" +
            "                    \"footer_type\": \"M\",\n" +
            "                    \"position\": 0,\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 243,\n" +
            "                                \"kind\": \"last_4_digits_of_your_social_security_number\",\n" +
            "                                \"name\": \"Last 4 Digits of your Social Security Number\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 246,\n" +
            "                                \"kind\": \"option_to_indicate_that_you_do_not_have_the_requested_id\",\n" +
            "                                \"name\": \"Option to Indicate that you do not have the Requested ID\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"item\": {\n" +
            "                                \"id\": 245,\n" +
            "                                \"kind\": \"us_state_or_territory_or_district_issued_id\",\n" +
            "                                \"name\": \"U.S. State or Territory or District Issued ID\"\n" +
            "                            },\n" +
            "                            \"position\": 0\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            ],\n" +
            "            \"witness_notarization_requirements\": [\n" +
            "            ],\n" +
            "            \"transmission_methods\": [\n" +
            "                {\n" +
            "                    \"id\": 2,\n" +
            "                    \"voter_type\": {\n" +
            "                        \"id\": 2,\n" +
            "                        \"kind\": \"O\",\n" +
            "                        \"name\": \"Overseas Voter\"\n" +
            "                    },\n" +
            "                    \"additional_info\": \"The absentee ballot's outside return envelope has an affidavit that must signed by two witnesses (at least 18 years old). Note that if using the Federal Write-In Absentee Ballot (FWAB), two witnesses are still required to sign the FWAB form.\",\n" +
            "                    \"additional_info_type\": \"M\",\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"id\": 2,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"blank_ballot_to_voter\",\n" +
            "                                \"name\": \"Blank Ballot To Voter\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 1,\n" +
            "                                \"kind\": \"in_person\",\n" +
            "                                \"name\": \"In-Person\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"id\": 3,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"blank_ballot_to_voter\",\n" +
            "                                \"name\": \"Blank Ballot To Voter\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 2,\n" +
            "                                \"kind\": \"mail\",\n" +
            "                                \"name\": \"Mail\"\n" +
            "                            },\n" +
            "                            \"allowed\": true,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"id\": 4,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"blank_ballot_to_voter\",\n" +
            "                                \"name\": \"Blank Ballot To Voter\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"fax\",\n" +
            "                                \"name\": \"Fax\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 3,\n" +
            "                    \"voter_type\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"D\",\n" +
            "                        \"name\": \"Domestic Voter\"\n" +
            "                    },\n" +
            "                    \"additional_info\": \"\",\n" +
            "                    \"additional_info_type\": \"M\",\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"id\": 35,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"blank_ballot_to_voter\",\n" +
            "                                \"name\": \"Blank Ballot To Voter\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 3,\n" +
            "                                \"kind\": \"email\",\n" +
            "                                \"name\": \"Email\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"id\": 36,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"blank_ballot_to_voter\",\n" +
            "                                \"name\": \"Blank Ballot To Voter\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 5,\n" +
            "                                \"kind\": \"online\",\n" +
            "                                \"name\": \"Online\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"id\": 37,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 2,\n" +
            "                                \"kind\": \"absentee_ballot_request\",\n" +
            "                                \"name\": \"Absentee Ballot Request\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 1,\n" +
            "                                \"kind\": \"in_person\",\n" +
            "                                \"name\": \"In-Person\"\n" +
            "                            },\n" +
            "                            \"allowed\": true,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 4,\n" +
            "                    \"voter_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"kind\": \"M\",\n" +
            "                        \"name\": \"Military Voter\"\n" +
            "                    },\n" +
            "                    \"additional_info\": \"If you are not registered to vote, you must return your voter registration and absentee ballot request by mail.\",\n" +
            "                    \"additional_info_type\": \"M\",\n" +
            "                    \"items\": [\n" +
            "                        {\n" +
            "                            \"id\": 68,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 2,\n" +
            "                                \"kind\": \"absentee_ballot_request\",\n" +
            "                                \"name\": \"Absentee Ballot Request\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 2,\n" +
            "                                \"kind\": \"mail\",\n" +
            "                                \"name\": \"Mail\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"id\": 69,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 2,\n" +
            "                                \"kind\": \"absentee_ballot_request\",\n" +
            "                                \"name\": \"Absentee Ballot Request\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 4,\n" +
            "                                \"kind\": \"fax\",\n" +
            "                                \"name\": \"Fax\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        },\n" +
            "                        {\n" +
            "                            \"id\": 70,\n" +
            "                            \"document_type\": {\n" +
            "                                \"id\": 2,\n" +
            "                                \"kind\": \"absentee_ballot_request\",\n" +
            "                                \"name\": \"Absentee Ballot Request\"\n" +
            "                            },\n" +
            "                            \"document_transmission_method\": {\n" +
            "                                \"id\": 3,\n" +
            "                                \"kind\": \"email\",\n" +
            "                                \"name\": \"Email\"\n" +
            "                            },\n" +
            "                            \"allowed\": false,\n" +
            "                            \"additional_info\": \"\",\n" +
            "                            \"additional_info_type\": \"M\"\n" +
            "                        }\n" +
            "                    ]\n" +
            "                }\n" +
            "            ],\n" +
            "            \"lookup_tools\": [\n" +
            "                {\n" +
            "                    \"id\": 2,\n" +
            "                    \"lookup_tool\": {\n" +
            "                        \"id\": 5,\n" +
            "                        \"kind\": \"election_information\",\n" +
            "                        \"name\": \"State Elections Website\"\n" +
            "                    },\n" +
            "                    \"url\": \"https://www.alabamavotes.gov/\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 3,\n" +
            "                    \"lookup_tool\": {\n" +
            "                        \"id\": 6,\n" +
            "                        \"kind\": \"sample_ballot\",\n" +
            "                        \"name\": \"Can I View my Sample Ballot?\"\n" +
            "                    },\n" +
            "                    \"url\": \"https://www.alabamavotes.gov/ElectionInfo/2016SampleBallotsPriRO.aspx?a=voters\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 4,\n" +
            "                    \"lookup_tool\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"kind\": \"polling_location\",\n" +
            "                        \"name\": \"Where is my Polling Place?\"\n" +
            "                    },\n" +
            "                    \"url\": \"https://myinfo.alabamavotes.gov/VoterView/PollingPlaceSearch.do\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 5,\n" +
            "                    \"lookup_tool\": {\n" +
            "                        \"id\": 2,\n" +
            "                        \"kind\": \"ballot_status\",\n" +
            "                        \"name\": \"Where is my Ballot?\"\n" +
            "                    },\n" +
            "                    \"url\": \"https://myinfo.alabamavotes.gov/VoterView/Home.do\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 6,\n" +
            "                    \"lookup_tool\": {\n" +
            "                        \"id\": 4,\n" +
            "                        \"kind\": \"online_registration\",\n" +
            "                        \"name\": \"Can I Register to Vote Online?\"\n" +
            "                    },\n" +
            "                    \"url\": \"https://www.alabamavotes.gov/olvr/default.aspx\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 7,\n" +
            "                    \"lookup_tool\": {\n" +
            "                        \"id\": 1,\n" +
            "                        \"kind\": \"registration_status\",\n" +
            "                        \"name\": \"Am I Registered?\"\n" +
            "                    },\n" +
            "                    \"url\": \"https://myinfo.alabamavotes.gov/VoterView/Home.do\"\n" +
            "                }\n" +
            "            ],\n" +
            "            \"voting_methods\": [\n" +
            "                {\n" +
            "                    \"id\": 3,\n" +
            "                    \"voting_method_type\": {\n" +
            "                        \"id\": 3,\n" +
            "                        \"kind\": \"absentee_voting_with_excuse\",\n" +
            "                        \"name\": \"Absentee Voting With Excuse\"\n" +
            "                    },\n" +
            "                    \"allowed\": true,\n" +
            "                    \"additional_info\": \"\",\n" +
            "                    \"additional_info_type\": \"M\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 4,\n" +
            "                    \"voting_method_type\": {\n" +
            "                        \"id\": 9,\n" +
            "                        \"kind\": \"overseas_absentee_voting\",\n" +
            "                        \"name\": \"Overseas Absentee Voting\"\n" +
            "                    },\n" +
            "                    \"allowed\": true,\n" +
            "                    \"additional_info\": \"Some additional Info\",\n" +
            "                    \"additional_info_type\": \"M\"\n" +
            "                },\n" +
            "                {\n" +
            "                    \"id\": 5,\n" +
            "                    \"voting_method_type\": {\n" +
            "                        \"id\": 7,\n" +
            "                        \"kind\": \"all_mail_voting\",\n" +
            "                        \"name\": \"All-Mail Voting\"\n" +
            "                    },\n" +
            "                    \"allowed\": false,\n" +
            "                    \"additional_info\": \"Some additional Info\",\n" +
            "                    \"additional_info_type\": \"M\"\n" +
            "                }\n" +
            "            ]\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    @Before
    public void setUpLocalElectionsService() {
        setLocalElectionsService( new LocalElectionsService() );
        getLocalElectionsService().setConnector( createMock( "LocalElectionsConnector", LocalElectionConnector.class ) );

    }

    public LocalElectionsService getLocalElectionsService() {
        return localElectionsService;
    }

    public void setLocalElectionsService( LocalElectionsService localElectionsService ) {
        this.localElectionsService = localElectionsService;
    }

    @Test
    public void testGetStates() {
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse( STATES ).getAsJsonObject();
        EasyMock.expect( getLocalElectionsService().getConnector().callMethod( EasyMock.eq("states"), EasyMock.<Map<String, String>>anyObject() ) )
                .andReturn( result );
        replayAll();

        final List<StateOfElection> states = getLocalElectionsService().getAllStates();

        assertEquals( "Should be 10 states", states.size(), 10 );
        assertEquals( "First state should be Alabama", states.get( 0 ).getName(), "Alabama" );
        verifyAll();
    }

    @Test
    public void testGetStateVoterInformation() {
        JsonParser parser = new JsonParser();
        JsonObject result = parser.parse( STATE_VOTER_INFORMATION ).getAsJsonObject();
        EasyMock.expect( getLocalElectionsService().getConnector().callMethod( EasyMock.eq("state_voter_information"), EasyMock.<Map<String, String>>anyObject() ) )
                .andReturn( result );
        replayAll();

        final List<StateVoterInformation> stateVoterInformationList = getLocalElectionsService().getAllStateVoterInformation();
        assertEquals( "Should be 1 state_voter_information", 1, stateVoterInformationList.size());

        StateVoterInformation stateVoterInformation = stateVoterInformationList.get( 0 );
        assertEquals( "First state should be Alabama", "Alabama", stateVoterInformation.getState().getName() );
        assertEquals( "eligibility_general_info should be empty string", "", stateVoterInformation.getEligibilityGeneralInfo() );
        assertEquals( "voting_general_info should be null", null, stateVoterInformation.getVotingGeneralInfo());
        assertEquals( "voter_id_general_info should be a string", "this is some voter_id_general_info", stateVoterInformation.getVoterIdGeneralInfo() );

        // eligibility_requirements
        assertEquals( "Should be 3 eligibilityRequirements", 3 , stateVoterInformation.getEligibilityRequirements().size());

        EligibilityRequirementsList eligibilityRequirementsList = stateVoterInformation.getEligibilityRequirements().get(2);
        assertEquals( "eligibilityRequirementsList should have voterType.name 'Domestic Voter'", "Domestic Voter" , eligibilityRequirementsList.getVoterType().getName());
        assertEquals( "eligibilityRequirementsList should have header", "Restorative Requirements" , eligibilityRequirementsList.getHeader());
        assertEquals( "eligibilityRequirementsList should have headerType", "M" , eligibilityRequirementsList.getHeaderType());
        assertEquals( "eligibilityRequirementsList should have footer", "Some Footer Text" , eligibilityRequirementsList.getFooter());
        assertEquals( "eligibilityRequirementsList should have headerType", "M" , eligibilityRequirementsList.getFooterType());
        assertEquals( "eligibilityRequirementsList should have position", 2 , eligibilityRequirementsList.getPosition());

        List<RequirementsListItem> items = eligibilityRequirementsList.getItems();
        assertEquals( "Should be 1 RequirementsListItem", 1 , items.size());

        RequirementsListItem item = items.get(0);
        assertEquals( "RequirementsListItem should have name", "If you have been previously convicted of a felony crime, you may vote in Alabama if your voting rights have been restored" , item.getName());
        assertEquals( "RequirementsListItem should have kind", "previously_convicted_of_felony_crime" , item.getKind());
        assertEquals( "RequirementsListItem should have position", 0 , item.getPosition());

        // identification_requirements
        assertEquals( "Should be 5 identificationRequirements", 5 , stateVoterInformation.getIdentificationRequirements().size());

        IdentificationRequirementsList identificationRequirementsList = stateVoterInformation.getIdentificationRequirements().get(3);
        assertEquals( "identificationRequirementsList should have category.name", "Voting Overseas" , identificationRequirementsList.getCategory().getName());
        assertEquals( "identificationRequirementsList should have category.name_format", "Voting Overseas formatted" , identificationRequirementsList.getCategory().getNameFormat());
        assertEquals( "identificationRequirementsList should have category.kind", "voting_overseas" , identificationRequirementsList.getCategory().getKind());
        assertEquals( "identificationRequirementsList should have header", "U.S. citizens living overseas may register and request a ballot using form (\"FPCA\")." , identificationRequirementsList.getHeader());
        assertEquals( "identificationRequirementsList should have headerType", "M" , identificationRequirementsList.getHeaderType());
        assertEquals( "identificationRequirementsList should have footer", "Some Footer Text" , identificationRequirementsList.getFooter());
        assertEquals( "identificationRequirementsList should have headerType", "M" , identificationRequirementsList.getFooterType());
        assertEquals( "identificationRequirementsList should have position", 0 , identificationRequirementsList.getPosition());

        List<RequirementsListItem> requirementsListItems = identificationRequirementsList.getItems();
        assertEquals( "Should be 1 RequirementsListItem", 3, requirementsListItems.size() );

        RequirementsListItem requirementsListItem = requirementsListItems.get(2);
        assertEquals( "RequirementsListItem should have name", "U.S. State or Territory or District Issued ID" , requirementsListItem.getName());
        assertEquals( "RequirementsListItem should have kind", "us_state_or_territory_or_district_issued_id" , requirementsListItem.getKind());
        assertEquals( "RequirementsListItem should have position", 0 , requirementsListItem.getPosition());

        //transmission_methods
        assertEquals( "Should be 3 transmissionMethods", 3 , stateVoterInformation.getTransmissionMethods().size());

        TransmissionMethodsList transmissionMethodsList = stateVoterInformation.getTransmissionMethods().get(2);
        assertEquals( "transmissionMethodsList should have voterType.name", "Military Voter" , transmissionMethodsList.getVoterType().getName());
        assertEquals( "transmissionMethodsList should have voterType.kind", "M" , transmissionMethodsList.getVoterType().getKind());
        assertEquals( "transmissionMethodsList should have additionalInfo", "If you are not registered to vote, you must return your voter registration and absentee ballot request by mail." , transmissionMethodsList.getAdditionalInfo());
        assertEquals( "transmissionMethodsList should have additionalInfo", "M" , transmissionMethodsList.getAdditionalInfoType());

        List<TransmissionMethodsListItem> transmissionMethodItems = transmissionMethodsList.getItems();
        assertEquals( "Should be 3 transmissionMethods", 3 , transmissionMethodItems.size());

        TransmissionMethodsListItem transmissionMethodItem = transmissionMethodItems.get(1);
        assertEquals( "transmissionMethodItem should have documentType.name", "Absentee Ballot Request" , transmissionMethodItem.getDocumentType().getName());
        assertEquals( "transmissionMethodItem should have documentType.kind", "absentee_ballot_request" , transmissionMethodItem.getDocumentType().getKind());
        assertEquals( "transmissionMethodItem should have documentTransmissionMethod.name", "Fax" , transmissionMethodItem.getDocumentTransmissionMethod().getName());
        assertEquals( "transmissionMethodItem should have documentTransmissionMethod.kind", "fax" , transmissionMethodItem.getDocumentTransmissionMethod().getKind());
        assertFalse("transmissionMethodItem should not be allowed", transmissionMethodItem.isAllowed());
        assertEquals( "transmissionMethodItem should have addtionalInfo", "" , transmissionMethodItem.getAdditionalInfo());
        assertEquals( "transmissionMethodItem should have addtionalInfoType", "M" , transmissionMethodItem.getAdditionalInfoType());

        //lookup_tools
        assertEquals( "Should be 6 lookupTools", 6 , stateVoterInformation.getLookupTools().size());

        LookupToolListItem lookupTool = stateVoterInformation.getLookupTools().get(3);
        assertEquals( "lookupTool should have URL", "https://www.alabamavotes.gov/olvr/default.aspx" , lookupTool.getUrl());
        assertEquals( "lookupTool should have name", "Can I Register to Vote Online?" , lookupTool.getName());
        assertEquals( "lookupTool should have name", "online_registration" , lookupTool.getKind());

        //voting_methods
        assertEquals( "Should be 3 votingMethods", 3 , stateVoterInformation.getVotingMethods().size());

        VotingMethod votingMethod = stateVoterInformation.getVotingMethods().get(1);
        assertEquals( "votingMethod should have votingMethodType.name", "All-Mail Voting" , votingMethod.getVotingMethodType().getName());
        assertEquals( "votingMethod should have votingMethodType.kind", "all_mail_voting" , votingMethod.getVotingMethodType().getKind());
        assertTrue( "votingMethod should not be allowed", !votingMethod.isAllowed());
        assertEquals( "lookupTool should have addtionalInfo", "Some additional Info" , votingMethod.getAdditionalInfo());
        assertEquals( "lookupTool should have addtionalInfoType", "M" , votingMethod.getAdditionalInfoType());

        verifyAll();
    }
 }
