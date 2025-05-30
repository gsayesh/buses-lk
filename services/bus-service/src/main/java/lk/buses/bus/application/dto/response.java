package lk.buses.bus.application.dto.response;

import lk.buses.common.core.enums.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

// Bus Operator Response
@Data
@Builder
public class BusOperatorResponse {
    private UUID id;
    private String operatorCode;
    private String operatorName;
    private BusOperatorType operatorType;
    private String contactNumber;
    private String email;
    private String address;
    private boolean isActive;
    private Integer busCount;
    private Instant createdAt;
}

// Bus Response
@Data
@Builder
public class BusResponse {
    private UUID id;
    private String registrationNumber;
    private BusOperatorResponse operator;
    private ServiceCategoryResponse serviceCategory;
    private String make;
    private String model;
    private Integer yearOfManufacture;
    private Integer seatingCapacity;
    private boolean hasGpsDevice;
    private String gpsDeviceId;
    private String busPhotoUrl;
    private boolean isActive;
    private Instant createdAt;
}

// Service Category Response
@Data
@Builder
public class ServiceCategoryResponse {
    private UUID id;
    private ServiceCategory category;
    private Double fareMultiplier;
    private String displayNameEn;
    private String displayNameSi;
    private String displayNameTa;
    private List<String> amenities;
}

// Bus Schedule Response
@Data
@Builder
public class BusScheduleResponse {
    private UUID id;
    private UUID routeId;
    private String routeNumber;
    private String routeName;
    private BusResponse bus;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private List<Integer> daysOfWeek;
    private List<String> operatingDays;
    private boolean isActive;
}

// Bus Location Response
@Data
@Builder
public class BusLocationResponse {
    private UUID busId;
    private String registrationNumber;
    private Double latitude;
    private Double longitude;
    private Double speedKmh;
    private Double heading;
    private String driverName;
    private TrackingSource trackingSource;
    private Instant lastUpdated;
    private boolean isMoving;
}

// Tracking History Response
@Data
@Builder
public class TrackingHistoryResponse {
    private UUID busId;
    private String registrationNumber;
    private List<TrackingPoint> trackingPoints;
    private Double totalDistance;
    private Double averageSpeed;
    private Double maxSpeed;
}

@Data
@Builder
public class TrackingPoint {
    private Double latitude;
    private Double longitude;
    private Double speedKmh;
    private Double heading;
    private Instant timestamp;
}

// Nearby Buses Response
@Data
@Builder
public class NearbyBusesResponse {
    private List<BusLocationWithDistance> buses;
    private Double searchRadiusKm;
    private Integer totalFound;
}

@Data
@Builder
public class BusLocationWithDistance {
    private BusLocationResponse busLocation;
    private Double distanceKm;
    private String direction;
}