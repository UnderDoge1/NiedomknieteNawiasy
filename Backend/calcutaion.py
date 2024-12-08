import math
from datetime import datetime

lighthouse1 = [2,4,0,0]
speed_of_light = 2
ship1 = [0,0,0,0,0]#[x,y,vx,vy,t]
ship2 = [2,2,1,1,0]

def position (ship,t):
    x = ship[0]+ship[2]*t
    y = ship[1]+ship[3]*t
    p = [x,y]
    return p

def relative_velocity(ship1,ship2):
    dvx = ship2[2] - ship1[2]
    dvy = ship2[3] - ship1[3]
    return [dvx,dvy]


def relative_position (object1, object2):
    x = object2[0]-object1[0]
    y = object2[1]-object1[1]
    p = [x,y]
    return p

def distance (object1, object2):
    x = relative_position(object1,object2)[0]
    y = relative_position(object1, object2)[1]
    d = math.hypot(y,x)
    return d

def relative_velocity (ship1, ship2):
    dvx = ship2[2] - ship1[2]
    dvy = ship2[3] - ship1[3]
    r_v = [dvx,dvy]
    return r_v

def ping_lighthouse_vector (ship,lighthouse):
    dx = relative_position(lighthouse, ship)[0]
    dy = relative_position(lighthouse,ship)[1]
    if dx == 0:
        a= math.pi/2
    else:
        a = math.tan(dy/dx)
    r = [math.cos(a), math.sin(a)]
    #zwraca wektor pingu do latarni

def register (ship,lighthouse):
    travel_time = distance(lighthouse,ship)/speed_of_light
    position = position(ship,travel_time)
    t = datetime.now()
    ship_reg = [ship[0],ship[1],position[0],position[1],t]
    return ship_reg
    #zwraca obecną pozycję i prędkość statku w czasie



def calculate_angle(object1,object2):

    time = 0
    t2 = time - object2[4]
    t1 = time - object1[4]
    p1 = position(object1, t1)
    p2 = position(object2, t2)


    # Relative position
    dx = p2[0] - p1[0]
    dy = p2[1] - p1[1]

    # Quadratic equation coefficients
    a = object2[2] ** 2 + object2[3] ** 2 - speed_of_light ** 2
    b = 2 * (dx * object2[2] + dy * object2[3])
    c = dx ** 2 + dy ** 2

    # Solve quadratic equation a*t^2 + b*t + c = 0
    discriminant = b ** 2 - 4 * a * c
    if discriminant < 0:
        raise ValueError("No solution: the target cannot be reached with the given projectile speed.")

    # Compute the smallest positive time of interception
    t1 = (-b + math.sqrt(discriminant)) / (2 * a)
    t2 = (-b - math.sqrt(discriminant)) / (2 * a)
    t = min(t for t in [t1, t2] if t > 0)

    # Target's position at time t
    xt = object2[0] + object2[2] * t
    yt = object2[1] + object2[3] * t

    # Angle calculation
    angle = math.atan2(yt - p1[1], xt - p1[0])/2
    return [angle,t]


ang = calculate_angle(ship1,ship2)[0]
t = calculate_angle(ship1,ship2)[1]
print(ship1)
print(ship2)
print('angle rad: '+str(ang))
print('angle deg: '+str(ang/math.pi*360))
print('time: '+str(t))






