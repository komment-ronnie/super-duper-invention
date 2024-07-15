const webhookHasAllPermissions = (webhook) => {
  let hasAll = true;
  WEBHOOK_PERMISSIONS.forEach((permission) => {

    console.log({ hasAll, permission, hasthis: webhook[permission] });
    hasAll = hasAll && webhook[permission];
  });
  return hasAll;
};