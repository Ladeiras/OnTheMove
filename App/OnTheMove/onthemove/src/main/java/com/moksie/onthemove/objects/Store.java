package com.moksie.onthemove.objects;

/**
 * Created by belh0 on 14-04-2014.
 */
public class Store
{
    private Contact contact;
    private Location location;
    private Campaign campaign;
    private boolean withCampaign;

    public Store(Contact contact, Location location, Campaign campaign, boolean withCampaign) {
        this.contact = contact;
        this.location = location;
        this.campaign = campaign;
        this.withCampaign = withCampaign;
    }

    public Store() {
        this.withCampaign = false;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }

    public boolean isWithCampaign() {
        return withCampaign;
    }

    public void setWithCampaign(boolean withCampaign) {
        this.withCampaign = withCampaign;
    }
}
